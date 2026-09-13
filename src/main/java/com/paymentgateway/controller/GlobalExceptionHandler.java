package com.paymentgateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.paymentgateway.dto.ErrorResponse;
import com.paymentgateway.exception.CannotCancelPaymentException;
import com.paymentgateway.exception.OrderAlreadyPaidException;
import com.paymentgateway.exception.OrderNotFoundException;
import com.paymentgateway.exception.PaymentNotFoundException;

@RestControllerAdvice 
public class GlobalExceptionHandler {
    
    @ExceptionHandler (OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse>handleOrderNotFound(OrderNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler (PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse>handlePaymentNotFound(PaymentNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(exception.getMessage()));
    }
    
    @ExceptionHandler (CannotCancelPaymentException.class)
    public ResponseEntity<ErrorResponse>handleCannotCancelPayment(CannotCancelPaymentException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler (OrderAlreadyPaidException.class)
    public ResponseEntity<ErrorResponse> handleOrderAlreadyPaidException(OrderAlreadyPaidException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(exception.getMessage()));
    }
}
