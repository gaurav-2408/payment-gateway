package com.paymentgateway.processor;

import java.math.BigDecimal;

public interface  PaymentProcessor {
    
    String processPayment(BigDecimal amount, String paymentMethodToken);
}
