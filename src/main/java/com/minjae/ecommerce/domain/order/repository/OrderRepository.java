package com.minjae.ecommerce.domain.order.repository;

import com.minjae.ecommerce.domain.order.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Optional<Orders> findByOrderNumber(String orderNumber);

    @Query("SELECT o FROM Orders o JOIN FETCH o.orderItems WHERE o.member.memberId = :memberId ORDER BY o.createdAt DESC")
    List<Orders> findAllByMemberIdWithItems(Long memberId);
}
