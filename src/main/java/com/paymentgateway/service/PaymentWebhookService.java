package com.paymentgateway.service;

import org.springframework.stereotype.Service;

import com.paymentgateway.dto.PaymentResponse;
import com.paymentgateway.dto.PaymentWebhookRequest;
import com.paymentgateway.entity.Payment;
import com.paymentgateway.entity.PaymentStatus;
import com.paymentgateway.exception.InvalidMerchantReferenceException;
import com.paymentgateway.repository.PaymentRespository;

@Service
public class PaymentWebhookService {

    private final PaymentRespository paymentRespository;

    public PaymentWebhookService(PaymentRespository paymentRespository) {
        this.paymentRespository = paymentRespository;
    }

    public PaymentResponse processWebhook(PaymentWebhookRequest request) {
        Payment payment = paymentRespository.findByIdempotencyKey(request.getMerchantReference())
                .orElseThrow(() -> new InvalidMerchantReferenceException());

        if (!PaymentStatus.PENDING.equals(payment.getStatus())) {
            return convertToResponse(payment);
        }

        if(PaymentStatus.SUCCESS.equals(request.getStatus())){
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setProcessorTransactionId(request.getProcessorTransactionId());
        }
        else if(PaymentStatus.FAILED.equals(request.getStatus())){
            payment.setStatus(PaymentStatus.FAILED);
        }

        payment = paymentRespository.save(payment);

        return convertToResponse(payment);
    }

    private PaymentResponse convertToResponse(Payment payment) {

        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getProcessorTransactionId());
    }
}
