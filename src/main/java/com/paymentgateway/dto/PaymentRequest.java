package com.paymentgateway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class PaymentRequest {
    
    @NotNull 
    private Long orderId;

    @NotBlank 
    private String paymentMethodToken;

    @NotBlank 
    private String idempotencyKey;

    // public Long getOrderId() {
    //     return orderId;
    // }

    // public void setOrderId(Long orderId) {
    //     this.orderId = orderId;
    // }

    // public String getPaymentMethodToken() {
    //     return paymentMethodToken;
    // }

    // public void setPaymentMethodToken(String paymentMethodToken) {
    //     this.paymentMethodToken = paymentMethodToken;
    // }

    // public String getIdempotencyKey() {
    //     return idempotencyKey;
    // }

    // public void setIdempotencyKey(String idempotencyKey) {
    //     this.idempotencyKey = idempotencyKey;
    // }

    
}
