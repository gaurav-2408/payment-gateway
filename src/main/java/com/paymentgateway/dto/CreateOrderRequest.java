package com.paymentgateway.dto;

import java.math.BigDecimal;

public class CreateOrderRequest {
    
    private BigDecimal amount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
}
