package com.paymentgateway.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.paymentgateway.dto.PaymentRequest;
import com.paymentgateway.dto.PaymentResponse;
import com.paymentgateway.entity.Order;
import com.paymentgateway.entity.Payment;
import com.paymentgateway.entity.PaymentStatus;
import com.paymentgateway.entity.Refund;
import com.paymentgateway.entity.RefundStatus;
import com.paymentgateway.exception.CannotCancelPaymentException;
import com.paymentgateway.exception.CannotRefundPaymentException;
import com.paymentgateway.exception.OrderAlreadyPaidException;
import com.paymentgateway.exception.OrderNotFoundException;
import com.paymentgateway.exception.PaymentNotFoundException;
import com.paymentgateway.processor.PaymentProcessor;
import com.paymentgateway.repository.OrderRepository;
import com.paymentgateway.repository.PaymentRespository;
import com.paymentgateway.repository.RefundRepository;

import jakarta.transaction.Transactional;

@Service
public class PaymentService {

    private final PaymentProcessor paymentProcessor;
    private final OrderRepository orderRepository;
    private final PaymentRespository paymentRespository;
    private final RefundRepository refundRepository;

    public PaymentService(
            PaymentProcessor paymentProcessor,
            OrderRepository orderRepository,
            PaymentRespository paymentRespository,
            RefundRepository refundRepository) {

        this.paymentProcessor = paymentProcessor;
        this.orderRepository = orderRepository;
        this.paymentRespository = paymentRespository;
        this.refundRepository = refundRepository;
    }

    @Transactional 
    public PaymentResponse processPayment(PaymentRequest request) {

        // 1. Idempotency check
        Optional<Payment> existingPayment =
                paymentRespository.findByIdempotencyKey(
                        request.getIdempotencyKey()
                );

        if (existingPayment.isPresent()) {
            return convertToResponse(existingPayment.get());
        }

        // 2. Validate order
        Order order = orderRepository
                .findById(request.getOrderId())
                .orElseThrow(() ->
                        new OrderNotFoundException(request.getOrderId())
                );
        
        //2.1 If payment with success status already exists then with changing idempotency key we were able to carry multiple payment for same order, so we handled this here
        Optional<Payment> successfulPayment = paymentRespository.findByOrderIdAndStatus(request.getOrderId(), PaymentStatus.SUCCESS);

        if(successfulPayment.isPresent())
            throw new OrderAlreadyPaidException(request.getOrderId());
        
        // 3. Create PROCESSING payment
        Payment payment = new Payment();

        payment.setOrderId(order.getId());
        payment.setAmount(order.getAmount());
        payment.setIdempotencyKey(request.getIdempotencyKey());
        payment.setStatus(PaymentStatus.PROCESSING);
        payment.setCreatedAt(LocalDateTime.now());

        payment = paymentRespository.save(payment);

        Long paymentId = payment.getId();

        try {

            // 4. External processor call - waits 25 seconds
            String processorTransactionId =
                    paymentProcessor.processPayment(
                            order.getAmount(),
                            request.getPaymentMethodToken()
                    );

            // 5. Set SUCCESS only if DB status is still PROCESSING
            paymentRespository.markSuccessIfProcessing(
                    paymentId,
                    processorTransactionId
            );

        } catch (Exception e) {

            // Set FAILED only if DB status is still PROCESSING
            paymentRespository.markFailedIfProcessing(paymentId);
        }

        // 6. Read final state
        Payment finalPayment = paymentRespository
                .findById(paymentId)
                .orElseThrow(() ->
                        new PaymentNotFoundException(paymentId)
                );

        return convertToResponse(finalPayment);
    }

    @Transactional 
    public PaymentResponse cancelPayment(Long paymentId) {

        Payment payment = paymentRespository
                .findById(paymentId)
                .orElseThrow(() ->
                        new PaymentNotFoundException(paymentId)
                );

        PaymentStatus paymentStatus = payment.getStatus();

        if (PaymentStatus.PROCESSING.equals(paymentStatus)) {

            payment.setStatus(PaymentStatus.CANCELLED);

        } else {

            throw new CannotCancelPaymentException(paymentId);
        }

        payment = paymentRespository.save(payment);

        return convertToResponse(payment);
    }

    public PaymentResponse getPayment(Long paymentId) {

        Payment payment = paymentRespository
                .findById(paymentId)
                .orElseThrow(() ->
                        new PaymentNotFoundException(paymentId)
                );

        return convertToResponse(payment);
    }

    public List<PaymentResponse>getAllPaymentsForOrderId (Long orderId){
        //check if order exists
        orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));

        //get all payments for the order    
        List<Payment> payments = paymentRespository.findByOrderId(orderId);

        //convert entities to response
        List<PaymentResponse>responses = new ArrayList<>();

        for(Payment payment: payments){
            responses.add(convertToResponse(payment));
        }

        return responses;
    }

    @Transactional 
    public PaymentResponse refundPayment(Long paymentId) {
        Payment payment = paymentRespository.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));

        if(!PaymentStatus.SUCCESS.equals(payment.getStatus())) 
            throw new CannotRefundPaymentException(paymentId);

        Refund refund = new Refund();
        refund.setPaymentId(paymentId);
        refund.setPaymentTransactionId(payment.getProcessorTransactionId());
        refund.setAmount(payment.getAmount());
        refund.setStatus(RefundStatus.SUCCESS);
        refund.setCreatedAt(LocalDateTime.now());
        refund.setRefundTransactionId("REF-" + UUID.randomUUID());

        refund = refundRepository.save(refund);

        payment.setStatus(PaymentStatus.REFUNDED);
        payment = paymentRespository.save(payment);

        return convertToResponse(payment);
    }

    private PaymentResponse convertToResponse(Payment payment) {

        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getProcessorTransactionId()
        );
    }
}