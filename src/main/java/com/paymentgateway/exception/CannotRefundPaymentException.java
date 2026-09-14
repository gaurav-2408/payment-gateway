package com.paymentgateway.exception;

public class CannotRefundPaymentException extends RuntimeException{
    public CannotRefundPaymentException (Long paymentId){
        super("Cannot refund this payment as its either failed/ processing/ cancelled or already refunded: " + paymentId);
    }
}
