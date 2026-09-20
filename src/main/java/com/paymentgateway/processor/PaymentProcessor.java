package com.paymentgateway.processor;

import com.paymentgateway.entity.PaymentStatus;

import java.math.BigDecimal;

public interface  PaymentProcessor {
    
    String processPayment(BigDecimal amount, String paymentMethodToken);
    
    PaymentStatus checkPaymentStatusInProcessor(String merchantReference);
}
