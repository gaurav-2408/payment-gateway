package com.paymentgateway.exception;

public class PaymentNotFoundException extends RuntimeException{

    public PaymentNotFoundException(Long paymentId) {
        super("Payment not found with id: " + paymentId);
    }
    
}
