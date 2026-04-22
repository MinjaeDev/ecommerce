package com.minjae.ecommerce.domain.payment.entity;

import com.minjae.ecommerce.domain.order.entity.Orders;
import com.minjae.ecommerce.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "payment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Orders orders;

    // PG사 거래 키
    @Column(unique = true, length = 200)
    private String paymentKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    // PG사 응답 원본 JSON 보존
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private Map<String, Object> pgResponse;

    private LocalDateTime paidAt;

    @Builder
    public Payment(Orders orders, PaymentMethod method, BigDecimal amount) {
        this.orders = orders;
        this.method = method;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }

    // 비즈니스 메서드
    public void complete(String paymentKey, Map<String, Object> pgResponse) {
        this.paymentKey = paymentKey;
        this.pgResponse = pgResponse;
        this.status = PaymentStatus.COMPLETED;
        this.paidAt = LocalDateTime.now();
    }

    public void fail() {
        this.status = PaymentStatus.FAILED;
    }

    public void cancel() {
        this.status = PaymentStatus.CANCELLED;
    }

    public void refund() {
        this.status = PaymentStatus.REFUNDED;
    }
}
