package com.paymentgateway.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paymentgateway.entity.ProcessorPayment;

public interface ProcessorPaymentRepository extends JpaRepository<ProcessorPayment, Long>{
    Optional<ProcessorPayment> findByMerchantRefernce(String merchantReference);
}
