package com.minjae.ecommerce.infra.kafka.producer;

import com.minjae.ecommerce.infra.kafka.event.DomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DomainEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(String topic, DomainEvent event) {
        kafkaTemplate.send(topic, String.valueOf(event.getAggregateId()), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("이벤트 발행 실패: type={}, aggregateId={}, error={}",
                                event.getEventType(), event.getAggregateId(), ex.getMessage());
                    } else {
                        log.info("이벤트 발행 완료: type={}, aggregateId={}, topic={}, partition={}, offset={}",
                                event.getEventType(),
                                event.getAggregateId(),
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}
