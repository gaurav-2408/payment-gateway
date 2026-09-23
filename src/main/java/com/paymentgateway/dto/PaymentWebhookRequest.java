package com.paymentgateway.dto;

import com.paymentgateway.entity.PaymentStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor 
public class PaymentWebhookRequest {
    
    @NotNull 
    private String eventId;

    private String merchantReference;

    private String processorTransactionId;

    private PaymentStatus status;

}
