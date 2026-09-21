package com.paymentgateway.dto;

import com.paymentgateway.entity.PaymentStatus;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor 
public class ProcessorPaymentResponse {
    
    private Long processorPaymentId;

    private String processorTransactionId;

    private String merchantReference;

    @Enumerated (EnumType.STRING)
    private PaymentStatus status;
}
