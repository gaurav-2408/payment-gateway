package com.paymentgateway.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paymentgateway.dto.CreateOrderRequest;
import com.paymentgateway.dto.PaymentResponse;
import com.paymentgateway.entity.Order;
import com.paymentgateway.repository.OrderRepository;
import com.paymentgateway.service.PaymentService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    public OrderController(OrderRepository orderRepository, PaymentService paymentService){
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
    }

    @PostMapping 
    public Order createOrder(@Valid  @RequestBody CreateOrderRequest request){
        Order order = new Order(request.getAmount());

        return orderRepository.save(order);
    }

    @GetMapping ("/{orderId}/payments")
    public ResponseEntity<List<PaymentResponse>> getAllPaymentsForOrderId(@PathVariable ("orderId") Long orderId){
        List<PaymentResponse>payments = paymentService.getAllPaymentsForOrderId(orderId);

        return ResponseEntity.ok(payments);
    }

}
