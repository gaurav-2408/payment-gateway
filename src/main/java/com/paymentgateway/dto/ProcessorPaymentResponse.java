package com.paymentgateway.dto;

import com.paymentgateway.entity.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor 
public class ProcessorPaymentResponse {
    
    private Long processorPaymentId;

    private String processorTransactionId;

    private String merchantReference;

    private PaymentStatus status;
}
