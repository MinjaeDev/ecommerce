package com.minjae.ecommerce.infra.kafka.listener;

import com.minjae.ecommerce.infra.kafka.producer.DomainEventProducer;
import com.minjae.ecommerce.infra.kafka.KafkaTopics;
import com.minjae.ecommerce.infra.kafka.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventListener {

    private final DomainEventProducer domainEventProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        log.info("트랜잭션 커밋 완료, kafka 발행 시작: orderId={}", event.getOrderId());
        domainEventProducer.publish(KafkaTopics.PAYMENT_COMPLETED, event);
    }
}
