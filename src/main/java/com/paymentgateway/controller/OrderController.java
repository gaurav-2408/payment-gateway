package com.paymentgateway.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paymentgateway.dto.CreateOrderRequest;
import com.paymentgateway.entity.Order;
import com.paymentgateway.repository.OrderRepository;

import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/orders")
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @PostMapping 
    public Order createOrder(@Valid  @RequestBody CreateOrderRequest request){
        Order order = new Order(request.getAmount());

        return orderRepository.save(order);
    }
}
