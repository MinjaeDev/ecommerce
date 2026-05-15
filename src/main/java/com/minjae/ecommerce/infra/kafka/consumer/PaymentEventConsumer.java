package com.minjae.ecommerce.infra.kafka.consumer;

import com.minjae.ecommerce.domain.order.entity.Orders;
import com.minjae.ecommerce.domain.order.repository.OrderRepository;
import com.minjae.ecommerce.global.exception.BusinessException;
import com.minjae.ecommerce.global.exception.ErrorCode;
import com.minjae.ecommerce.infra.kafka.KafkaTopics;
import com.minjae.ecommerce.infra.kafka.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final OrderRepository orderRepository;

    @KafkaListener(topics = KafkaTopics.PAYMENT_COMPLETED, groupId = "order-status-group")
    @Transactional
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        log.info("결제 완료 이벤트 수신: orderId={}, paymentKey={}", event.getOrderId(), event.getPaymentKey());

        Orders orders = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        orders.preparing();

        log.info("주문 상태 변경 완료: orderId={}, status={}", orders.getOrderId(), orders.getStatus());
    }
}
