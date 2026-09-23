package com.paymentgateway.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.paymentgateway.dto.PaymentRequest;
import com.paymentgateway.entity.Order;
import com.paymentgateway.entity.Payment;
import com.paymentgateway.entity.PaymentStatus;
import com.paymentgateway.exception.OrderAlreadyPaidException;
import com.paymentgateway.exception.OrderNotFoundException;
import com.paymentgateway.repository.OrderRepository;
import com.paymentgateway.repository.PaymentRespository;

import jakarta.transaction.Transactional;

@Service 
public class PaymentCreationService {

    private final OrderRepository orderRepository;
    private final PaymentRespository paymentRespository;

    public PaymentCreationService(OrderRepository orderRepository,
            PaymentRespository paymentRespository) {
        this.orderRepository = orderRepository;
        this.paymentRespository = paymentRespository;
    }

    @Transactional 
    public Payment createProcessingPayment(PaymentRequest request) {

        Order order = orderRepository.findWithLockById(request.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException(request.getOrderId()));
            
        System.out.println("Lock acquired for order: " + order.getId());

        boolean hasBlockingPayment = paymentRespository.existsByOrderIdAndStatusIn(
                request.getOrderId(),
                List.of(
                        PaymentStatus.SUCCESS,
                        PaymentStatus.PROCESSING,
                        PaymentStatus.PENDING,
                        PaymentStatus.REFUNDED));

        if (hasBlockingPayment) {
            throw new OrderAlreadyPaidException(request.getOrderId());
        }

        // 3. Create PROCESSING payment
        Payment payment = new Payment();

        payment.setOrderId(order.getId());
        payment.setAmount(order.getAmount());
        payment.setIdempotencyKey(request.getIdempotencyKey());
        payment.setStatus(PaymentStatus.PROCESSING);
        payment.setCreatedAt(LocalDateTime.now());

        payment = paymentRespository.save(payment);

        return payment;
    }
}
