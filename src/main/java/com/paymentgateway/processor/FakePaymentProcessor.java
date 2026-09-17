package com.paymentgateway.processor;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.paymentgateway.exception.InvalidTokenException;

import java.util.UUID;

@Component
public class FakePaymentProcessor implements PaymentProcessor {
    @Override
    public String processPayment(BigDecimal amount, String paymentMethodToken) {

        System.out.println("Calling External Payment Processor");
        System.out.println("Charging amount: " + amount);

        switch (paymentMethodToken) {

            case "tok_success":
                return "TXN-" + UUID.randomUUID();

            case "tok_fail":
                throw new RuntimeException(
                        "Payment declined by processor");

            case "tok_timeout":
                try {
                    Thread.sleep(15000); // simulate slow external processor
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();

                    throw new RuntimeException(
                            "Payment processing interrupted", e);
                }

                throw new RuntimeException(
                        "Payment processor timed out");

            default:
                throw new InvalidTokenException(paymentMethodToken);
        }
    }
}