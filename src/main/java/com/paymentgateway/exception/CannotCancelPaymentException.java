package com.paymentgateway.exception;

public class CannotCancelPaymentException extends RuntimeException{
    
    public CannotCancelPaymentException(Long paymentId){
        super("Cannot cancel this order now with payment Id " + paymentId);
    }
}
