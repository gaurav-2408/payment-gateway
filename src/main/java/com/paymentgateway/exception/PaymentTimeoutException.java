package com.paymentgateway.exception;

public class PaymentTimeoutException extends RuntimeException{

    public PaymentTimeoutException(Long paymentId){
        super("Payment for this order timeed out, payment id: " + paymentId);
    }
}
