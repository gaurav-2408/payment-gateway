package com.paymentgateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paymentgateway.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

    
}
