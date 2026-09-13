package com.paymentgateway.processor;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class FakePaymentProcessor implements PaymentProcessor {

    @Override
    public String processPayment(BigDecimal amount, String paymentMethodToken) {

        System.out.println("Calling External Payment Processor");
        System.out.println("Charging amount: " + amount);

        // try {
        //     Thread.sleep(25000); // 25 seconds
        // } catch (InterruptedException e) {
        //     Thread.currentThread().interrupt();
        // }

        return "TXN-" + UUID.randomUUID();
    }
}