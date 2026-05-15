package com.minjae.ecommerce.domain.payment.service;

import com.minjae.ecommerce.api.payment.request.PaymentCancelRequest;
import com.minjae.ecommerce.api.payment.request.PaymentRequest;
import com.minjae.ecommerce.api.payment.response.PaymentResponse;
import com.minjae.ecommerce.domain.order.entity.Orders;
import com.minjae.ecommerce.domain.order.repository.OrderRepository;
import com.minjae.ecommerce.domain.payment.entity.Payment;
import com.minjae.ecommerce.domain.payment.entity.PaymentCancel;
import com.minjae.ecommerce.domain.payment.entity.PaymentStatus;
import com.minjae.ecommerce.domain.payment.repository.PaymentCancelRepository;
import com.minjae.ecommerce.domain.payment.repository.PaymentRepository;
import com.minjae.ecommerce.global.exception.BusinessException;
import com.minjae.ecommerce.global.exception.ErrorCode;
import com.minjae.ecommerce.infra.kafka.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentCancelRepository paymentCancelRepository;
    private final OrderRepository orderRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public PaymentResponse requestPayment(String publicId, PaymentRequest request) {
        Orders orders = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        //주문 소유자 검증
        if (!orders.getMember().getPublicId().equals(publicId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        //이미 결제된 주문인지 확인
        paymentRepository.findByOrders_OrderId(orders.getOrderId())
                .ifPresent(p -> {
                    if (p.getStatus() == PaymentStatus.COMPLETED)
                        throw new BusinessException(ErrorCode.PAYMENT_ALREADY_COMPLETED);
                });

        Payment payment = Payment.builder()
                .orders(orders)
                .method(request.getMethod())
                .amount(orders.getFinalAmount())
                .build();

        paymentRepository.save(payment);

        // PG사 결제 승인 시뮬레이션
        // 실제 서비스에서는 PG사 API 호출
        String fakePaymentKey = "PAY-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
        Map<String, Object> fakePgResponse = Map.of(
                "paymentKey", fakePaymentKey,
                "status", "DONE",
                "method", request.getMethod().name(),
                "totalAmount", orders.getFinalAmount(),
                "approvedAt", java.time.LocalDateTime.now().toString()
        );

        payment.complete(fakePaymentKey, fakePgResponse);
        orders.paid();

        // spring 이벤트로 발행 (커밋 후 kafka 발행)
        applicationEventPublisher.publishEvent(new PaymentCompletedEvent(
                orders.getOrderId(),
                payment.getPaymentId(),
                fakePaymentKey,
                payment.getAmount(),
                request.getMethod().name(),
                payment.getPaidAt()
        ));

        log.info("결제 완료: orderId={}, paymentKey={}", orders.getOrderId(), fakePaymentKey);

        return new PaymentResponse(payment);
    }

    public PaymentResponse getPayment(Long orderId) {
        Payment payment = paymentRepository.findByOrders_OrderId(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PAYMENT_NOT_FOUND));
        return new PaymentResponse(payment);
    }

    @Transactional
    public PaymentResponse cancelPayment(Long paymentId, PaymentCancelRequest request) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PAYMENT_NOT_FOUND));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS);
        }

        // PG사 취소 시뮬레이션
        String fakeCancelKey = "CANCEL-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();

        PaymentCancel cancel = PaymentCancel.builder()
                .payment(payment)
                .cancelReason(request.getCancelReason())
                .cancelAmount(request.getCancelAmount())
                .cancelKey(fakeCancelKey)
                .build();

        paymentCancelRepository.save(cancel);
        payment.cancel();
        payment.getOrders().cancel();

        log.info("결제 취소: paymentId={}, cancelKey={}", paymentId, fakeCancelKey);

        return new PaymentResponse(payment);
    }
}
