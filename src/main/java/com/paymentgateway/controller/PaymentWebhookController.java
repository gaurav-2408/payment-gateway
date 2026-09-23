package com.paymentgateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paymentgateway.dto.PaymentResponse;
import com.paymentgateway.dto.PaymentWebhookRequest;
import com.paymentgateway.service.PaymentWebhookService;

@RestController 
@RequestMapping ("/webhooks")
public class PaymentWebhookController {
    
    private final PaymentWebhookService paymentWebhookService;

    public PaymentWebhookController(PaymentWebhookService paymentWebhookService){
        this.paymentWebhookService = paymentWebhookService;
    }

    @PostMapping ("/payment")
    public ResponseEntity<PaymentResponse> processWebhook(@RequestBody PaymentWebhookRequest request){
        PaymentResponse response = paymentWebhookService.processWebhook(request);

        return ResponseEntity.ok(response);
    }
}
