package com.paymentgateway.exception;

public class InvalidTokenException extends RuntimeException{
    
    public InvalidTokenException(String paymentMethodToken){
        super("Provided payment token is invalid: " + paymentMethodToken);
    }
}
