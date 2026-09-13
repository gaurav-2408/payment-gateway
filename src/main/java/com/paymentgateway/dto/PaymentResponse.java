package com.paymentgateway.dto;

import java.math.BigDecimal;

import com.paymentgateway.entity.PaymentStatus;

public class PaymentResponse {
    
    private Long paymentId;

    private Long orderId;

    private BigDecimal amount;

    private PaymentStatus status;

    private String paymentTransactionId;

    public PaymentResponse(Long paymentId, Long orderId, BigDecimal amount, PaymentStatus status,
            String paymentTransactionId) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.paymentTransactionId = paymentTransactionId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getPaymentTransactionId() {
        return paymentTransactionId;
    }

    

}
