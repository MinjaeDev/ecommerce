package com.minjae.ecommerce.infra.kafka.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCompletedEvent implements DomainEvent {

    private Long orderId;
    private Long paymentId;
    private String paymentKey;
    private BigDecimal amount;
    private String method;
    private LocalDateTime paidAt;

    @Override
    @JsonIgnore
    public String getEventType() {
        return "PAYMENT_COMPLETED";
    }

    @Override
    @JsonIgnore
    public Long getAggregateId() {
        return orderId;
    }

    @Override
    @JsonIgnore
    public LocalDateTime getOccurredAt() {
        return paidAt;
    }
}
