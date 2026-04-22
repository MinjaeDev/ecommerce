package com.minjae.ecommerce.domain.order.entity;

public enum OrderStatus {
    PENDING,    // 주문 생성 (결제 대기)
    PAID,       // 결제 완료
    PREPARING,  // 상품 준비 중
    SHIPPED,    // 배송 중
    DELIVERED,  // 배송 완료
    CANCELLED   // 주문 취소
}
