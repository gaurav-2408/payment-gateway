package com.paymentgateway.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.paymentgateway.dto.PaymentRequest;
import com.paymentgateway.dto.PaymentResponse;
import com.paymentgateway.entity.Order;
import com.paymentgateway.entity.Payment;
import com.paymentgateway.entity.PaymentStatus;
import com.paymentgateway.exception.CannotCancelPaymentException;
import com.paymentgateway.exception.OrderNotFoundException;
import com.paymentgateway.exception.PaymentNotFoundException;
import com.paymentgateway.processor.PaymentProcessor;
import com.paymentgateway.repository.OrderRepository;
import com.paymentgateway.repository.PaymentRespository;

@Service
public class PaymentService {

    private final PaymentProcessor paymentProcessor;
    private final OrderRepository orderRepository;
    private final PaymentRespository paymentRespository;

    public PaymentService(
            PaymentProcessor paymentProcessor,
            OrderRepository orderRepository,
            PaymentRespository paymentRespository) {

        this.paymentProcessor = paymentProcessor;
        this.orderRepository = orderRepository;
        this.paymentRespository = paymentRespository;
    }

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