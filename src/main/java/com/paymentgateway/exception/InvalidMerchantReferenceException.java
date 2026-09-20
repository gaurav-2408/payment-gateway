package com.paymentgateway.exception;

public class InvalidMerchantReferenceException extends RuntimeException{
    
    public InvalidMerchantReferenceException(){
        super("Invalid Merchant Reference");
    }
}
