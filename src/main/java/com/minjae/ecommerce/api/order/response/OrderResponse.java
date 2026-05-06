package com.minjae.ecommerce.api.order.response;

import com.minjae.ecommerce.domain.order.entity.OrderItem;
import com.minjae.ecommerce.domain.order.entity.Orders;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderResponse {

    private final Long orderId;
    private final String orderNumber;
    private final String status;
    private final BigDecimal totalAmount;
    private final BigDecimal finalAmount;
    private final String recipientName;
    private final String recipientPhone;
    private final String deliveryAddress;
    private final List<OrderItemResponse> orderItems;
    private final LocalDateTime createdAt;

    public OrderResponse(Orders orders) {
        this.orderId = orders.getOrderId();
        this.orderNumber = orders.getOrderNumber();
        this.status = orders.getStatus().name();
        this.totalAmount = orders.getTotalAmount();
        this.finalAmount = orders.getFinalAmount();
        this.recipientName = orders.getRecipientName();
        this.recipientPhone = orders.getRecipientPhone();
        this.deliveryAddress = orders.getDeliveryAddress();
        this.orderItems = orders.getOrderItems().stream()
                .map(OrderItemResponse::new)
                .toList();
        this.createdAt = orders.getCreatedAt();
    }

    @Getter
    public static class OrderItemResponse {
        private final Long orderItemId;
        private final Long productId;
        private final String productName;
        private final BigDecimal productPrice;
        private final int quantity;
        private final BigDecimal subtotalAmount;

        public OrderItemResponse(OrderItem item) {
            this.orderItemId = item.getOrderItemId();
            this.productId = item.getProduct().getProductId();
            this.productName = item.getProductName();
            this.productPrice = item.getProductPrice();
            this.quantity = item.getQuantity();
            this.subtotalAmount = item.getSubtotalAmount();
        }
    }
}
