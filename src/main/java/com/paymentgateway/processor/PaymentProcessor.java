package com.paymentgateway.processor;

import com.paymentgateway.dto.ProcessorPaymentResponse;

import java.math.BigDecimal;

public interface  PaymentProcessor {
    
    String processPayment(BigDecimal amount, String paymentMethodToken);
    
    ProcessorPaymentResponse checkPaymentStatusInProcessor(String merchantReference);
}
