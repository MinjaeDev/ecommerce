package com.minjae.ecommerce.domain.order.repository;

import com.minjae.ecommerce.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrders_OrderId(Long orderId);
}
