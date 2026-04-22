package com.minjae.ecommerce.domain.payment.repository;

import com.minjae.ecommerce.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrders_OrderId(Long orderId);
    Optional<Payment> findByPaymentKey(String paymentKey);
}
