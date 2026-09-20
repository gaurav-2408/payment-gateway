package com.paymentgateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.paymentgateway.dto.ErrorResponse;
import com.paymentgateway.exception.CannotCancelPaymentException;
import com.paymentgateway.exception.CannotReconcilePaymentException;
import com.paymentgateway.exception.CannotRefundPaymentException;
import com.paymentgateway.exception.InvalidTokenException;
import com.paymentgateway.exception.OrderAlreadyPaidException;
import com.paymentgateway.exception.OrderNotFoundException;
import com.paymentgateway.exception.PaymentDeclinedException;
import com.paymentgateway.exception.PaymentNotFoundException;
import com.paymentgateway.exception.PaymentTimeoutException;

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

    @ExceptionHandler (CannotRefundPaymentException.class)
    public ResponseEntity<ErrorResponse> handleCannotRefundPaymentException(CannotRefundPaymentException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler (InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException exception){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler (PaymentTimeoutException.class)
    public ResponseEntity<ErrorResponse> handlePaymentTimeoutException(PaymentTimeoutException exception){
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler (PaymentDeclinedException.class)
    public ResponseEntity<ErrorResponse> handlePaymentDeclinedException(PaymentDeclinedException exception){
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler (CannotReconcilePaymentException.class)
    public ResponseEntity<ErrorResponse> handleCannotReconcilePaymentException(CannotReconcilePaymentException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(exception.getMessage()));
    }
}
  