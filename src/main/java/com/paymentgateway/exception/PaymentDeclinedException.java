package com.paymentgateway.exception;

public class PaymentDeclinedException extends RuntimeException{
    
    public PaymentDeclinedException(Long paymentId){
        super();
    }
}
