package com.paymentgateway.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity 
@Table (name = "refunds")
@Data 
@NoArgsConstructor 
public class Refund {
    
    @Id @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private Long paymentId;

    private BigDecimal amount;

    @Enumerated (EnumType.STRING)
    private PaymentStatus status;

    private String paymentTransactionId;
}
