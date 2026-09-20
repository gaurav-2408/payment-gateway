package com.paymentgateway.processor;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.paymentgateway.exception.InvalidMerchantReferenceException;
import com.paymentgateway.exception.InvalidTokenException;
import com.paymentgateway.exception.PaymentDeclinedException;
import com.paymentgateway.exception.PaymentTimeoutException;
import com.paymentgateway.repository.ProcessorPaymentRepository;

import com.paymentgateway.entity.PaymentStatus;
import com.paymentgateway.entity.ProcessorPayment;

import java.util.Random;
import java.util.UUID;

@Component
public class FakePaymentProcessor implements PaymentProcessor {

    private final ProcessorPaymentRepository processorPaymentRepository;

    public FakePaymentProcessor(ProcessorPaymentRepository processorPaymentRepository) {
        this.processorPaymentRepository = processorPaymentRepository;
    }

    private PaymentStatus generateProcessorOutcome() {

        int random = new Random().nextInt(100);

        if (random >= 50) {
            return PaymentStatus.SUCCESS;
        } else {
            return PaymentStatus.FAILED;
        }
    }

    @Override
    public String processPayment(BigDecimal amount, String paymentMethodToken) {

        System.out.println("Calling External Payment Processor");
        System.out.println("Charging amount: " + amount);

        try {
            Thread.sleep(15000); // simulate slow external processor
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        switch (paymentMethodToken) {

            case "tok_success":
                return "TXN-" + UUID.randomUUID();

            case "tok_fail":
                throw new PaymentDeclinedException();

            case "tok_timeout":
                try {
                    ProcessorPayment processorPayment = new ProcessorPayment();

                    processorPayment.setStatus(generateProcessorOutcome());
                    processorPayment.setMerchantReference(paymentMethodToken);
                    processorPayment.setProcessorTransactionId("TXN-" + UUID.randomUUID());
                    // processorPayment.setOrderId(orderid);

                    processorPaymentRepository.save(processorPayment);

                    Thread.sleep(3000); // simulate timeout scenario
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                throw new PaymentTimeoutException(null);

            default:
                throw new InvalidTokenException(paymentMethodToken);
        }
    }

    @Override
    public PaymentStatus checkPaymentStatusInProcessor(String merchantReference) {
        ProcessorPayment processorPayment = processorPaymentRepository
                .findByMerchantReference(merchantReference)
                .orElseThrow(() -> new InvalidMerchantReferenceException());

        return processorPayment.getStatus();

    }

}