package com.paymentgateway.exception;

public class PaymentDeclinedException extends RuntimeException{
    
    public PaymentDeclinedException(){
        super("Payment for this order is declined");
    }
}
