package com.paymentgateway.processor;

import com.paymentgateway.dto.ProcessorPaymentResponse;

import java.math.BigDecimal;

public interface  PaymentProcessor {
    
    String processPayment(BigDecimal amount, String paymentMethodToken, String merchantReference);
    
    ProcessorPaymentResponse checkPaymentStatusInProcessor(String merchantReference);
}
