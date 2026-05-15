package com.minjae.ecommerce.infra.kafka.event;

import java.time.LocalDateTime;

public interface DomainEvent {
    String getEventType();
    Long getAggregateId();
    LocalDateTime getOccurredAt();
}
