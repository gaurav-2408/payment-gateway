package com.paymentgateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paymentgateway.dto.PaymentRequest;
import com.paymentgateway.dto.PaymentResponse;
import com.paymentgateway.service.PaymentService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/payments")
public class PaymentController {
    
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @PostMapping 
    public ResponseEntity<PaymentResponse>processPayment(
        @Valid @RequestBody PaymentRequest request
    ){
        PaymentResponse response = paymentService.processPayment(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(
            @PathVariable("paymentId") Long paymentId) {

        return ResponseEntity.ok(
                paymentService.getPayment(paymentId)
        );
    }

    @PatchMapping ("/{paymentId}/cancel")
    public ResponseEntity<PaymentResponse>cancelPayment(@PathVariable ("paymentId") Long paymentId){
        return ResponseEntity.ok(paymentService.cancelPayment(paymentId));
    }

    @PostMapping ("/{paymentId}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(@Valid @PathVariable ("paymentId") Long PaymentId){
        PaymentResponse payment = paymentService.refundPayment(PaymentId);

        return ResponseEntity.ok(payment);
    }

    @PostMapping ("/{paymentId}/reconcile")
    public ResponseEntity<PaymentResponse> reconcilePayment(@Valid @PathVariable("paymentId") Long paymentId){
        PaymentResponse paymentResponse = paymentService.reconcilePayment(paymentId);

        return ResponseEntity.ok(paymentResponse);
    }
}
