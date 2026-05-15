package com.minjae.ecommerce.infra.kafka.consumer;

import com.minjae.ecommerce.infra.kafka.KafkaTopics;
import com.minjae.ecommerce.infra.kafka.event.PaymentCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationConsumer {

    @KafkaListener(topics = KafkaTopics.PAYMENT_COMPLETED, groupId = "notification-group")
    public void handlePaymentNotification(PaymentCompletedEvent event) {
        log.info("[알림] 결제 완료 알림 발송 시작: orderId={}", event.getOrderId());

        try {
            // 외부 이메일/SMS API 호출 시뮬레이션 (1초 소요)
            Thread.sleep(1000);

            log.info("[알림] 결제 완료 알림 발송 성공: orderId={}, amount={}",
                    event.getOrderId(), event.getAmount());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("[알림] 알림 발송 실패: orderId={}", event.getOrderId());
        }
    }
}
