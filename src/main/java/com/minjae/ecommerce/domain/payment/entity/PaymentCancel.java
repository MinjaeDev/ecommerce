package com.minjae.ecommerce.domain.payment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_cancel")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentCancel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cancelId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(nullable = false, length = 500)
    private String cancelReason;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cancelAmount;

    @Column(length = 200)
    private String cancelKey;

    @Column(nullable = false)
    private LocalDateTime cancelledAt;

    @PrePersist
    protected void onCreate() {
        this.cancelledAt = LocalDateTime.now();
    }

    @Builder
    public PaymentCancel(Payment payment, String cancelReason,
                         BigDecimal cancelAmount, String cancelKey) {
        this.payment = payment;
        this.cancelReason = cancelReason;
        this.cancelAmount = cancelAmount;
        this.cancelKey = cancelKey;
    }
}
