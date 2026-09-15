package com.paymentgateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paymentgateway.entity.Refund;

public interface RefundRepository extends JpaRepository<Refund, Long>{
    
}
