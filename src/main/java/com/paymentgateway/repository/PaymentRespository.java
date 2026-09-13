package com.paymentgateway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.paymentgateway.entity.Payment;

public interface PaymentRespository
        extends JpaRepository<Payment, Long> {

    Optional<Payment> findByIdempotencyKey(String idempotencyKey);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Payment p
        SET p.status = com.paymentgateway.entity.PaymentStatus.SUCCESS,
            p.processorTransactionId = :transactionId
        WHERE p.id = :paymentId
        AND p.status = com.paymentgateway.entity.PaymentStatus.PROCESSING
    """)
    int markSuccessIfProcessing(
            @Param("paymentId") Long paymentId,
            @Param("transactionId") String transactionId
    );

    @Modifying
    @Transactional
    @Query("""
        UPDATE Payment p
        SET p.status = com.paymentgateway.entity.PaymentStatus.FAILED
        WHERE p.id = :paymentId
        AND p.status = com.paymentgateway.entity.PaymentStatus.PROCESSING
    """)
    int markFailedIfProcessing(
            @Param("paymentId") Long paymentId
    );
}