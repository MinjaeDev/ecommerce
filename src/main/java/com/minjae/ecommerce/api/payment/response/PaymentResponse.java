package com.minjae.ecommerce.api.payment.response;

import com.minjae.ecommerce.domain.payment.entity.Payment;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class PaymentResponse {

    private final Long paymentId;
    private final Long orderId;
    private final String paymentKey;
    private final String method;
    private final BigDecimal amount;
    private final String status;
    private final LocalDateTime paidAt;
    private final LocalDateTime createdAt;

    public PaymentResponse(Payment payment) {
        this.paymentId = payment.getPaymentId();
        this.orderId = payment.getOrders().getOrderId();
        this.paymentKey = payment.getPaymentKey();
        this.method = payment.getMethod().name();
        this.amount = payment.getAmount();
        this.status = payment.getStatus().name();
        this.paidAt = payment.getPaidAt();
        this.createdAt = payment.getCreatedAt();
    }
}
