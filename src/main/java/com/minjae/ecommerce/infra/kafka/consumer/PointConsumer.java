package com.minjae.ecommerce.infra.kafka.consumer;

import com.minjae.ecommerce.domain.member.entity.Member;
import com.minjae.ecommerce.domain.member.repository.MemberRepository;
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

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointConsumer {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    //결제 금액의 1% 적립
    private static final BigDecimal POINT_RATE = new BigDecimal("0.01");

    @KafkaListener(topics = KafkaTopics.PAYMENT_COMPLETED, groupId = "point-group")
    @Transactional
    public void handlePointAccumulation(PaymentCompletedEvent event) {
        log.info("[포인트] 적립 시작: orderId={}", event.getOrderId());

        Orders orders = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        Member member = orders.getMember();

        int pointAmount = event.getAmount()
                .multiply(POINT_RATE)
                .setScale(0, RoundingMode.DOWN)
                .intValue();

        member.addPoint(pointAmount);

        log.info("[포인트] 적립 완료: memberId={}, addedPoint={}, totalPoint={}",
                member.getMemberId(), pointAmount, member.getPoint());
    }
}
