package com.paymentgateway.exception;

public class CannotReconcilePaymentException extends RuntimeException{

    public CannotReconcilePaymentException(Long payemntId){
        super("Cannot Reconcile this payment with id: " + payemntId);
    }
}
