package com.minjae.ecommerce.domain.payment.repository;

import com.minjae.ecommerce.domain.payment.entity.PaymentCancel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentCancelRepository extends JpaRepository<PaymentCancel, Long> {
    List<PaymentCancel> findAllByPayment_PaymentId(Long paymentId);
}
