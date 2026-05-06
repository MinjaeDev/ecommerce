package com.minjae.ecommerce.domain.order.service;

import com.minjae.ecommerce.api.order.request.CreateOrderRequest;
import com.minjae.ecommerce.api.order.response.OrderResponse;
import com.minjae.ecommerce.domain.member.entity.Member;
import com.minjae.ecommerce.domain.member.entity.MemberAddress;
import com.minjae.ecommerce.domain.member.repository.MemberAddressRepository;
import com.minjae.ecommerce.domain.member.repository.MemberRepository;
import com.minjae.ecommerce.domain.order.entity.OrderItem;
import com.minjae.ecommerce.domain.order.entity.Orders;
import com.minjae.ecommerce.domain.order.repository.OrderRepository;
import com.minjae.ecommerce.domain.product.entity.Product;
import com.minjae.ecommerce.domain.product.repository.ProductRepository;
import com.minjae.ecommerce.domain.product.repository.StockRepository;
import com.minjae.ecommerce.global.exception.BusinessException;
import com.minjae.ecommerce.global.exception.ErrorCode;
import com.minjae.ecommerce.infra.redis.StockLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final MemberAddressRepository memberAddressRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;
    private final StockLockService stockLockService;

    @Transactional
    public OrderResponse createOrder(String publicId, CreateOrderRequest request) {
        Member member = memberRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        MemberAddress address = memberAddressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        // 상품별 재고 확인 및 차감 (Redis 분산 락)
        for (CreateOrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

            // 재고 차감
            stockLockService.decreaseWithLock(product.getProductId(), itemRequest.getQuantity());

            BigDecimal subtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(subtotal);

            orderItems.add(OrderItem.builder()
                    .product(product)
                    .productName(product.getName())
                    .productPrice(product.getPrice())
                    .quantity(itemRequest.getQuantity())
                    .build());
        }

        // 주문 생성
        Orders orders = Orders.builder()
                .member(member)
                .orderNumber(generateOrderNumber())
                .totalAmount(totalAmount)
                .finalAmount(totalAmount)
                .recipientName(address.getRecipientName())
                .recipientPhone(address.getPhone())
                .deliveryAddress(address.getAddress1() + " " + address.getAddress2())
                .build();

        orderRepository.save(orders);

        // 주문 아이템 연결
        for (OrderItem item : orderItems) {
            OrderItem savedItem = OrderItem.builder()
                    .orders(orders)
                    .product(item.getProduct())
                    .productName(item.getProductName())
                    .productPrice(item.getProductPrice())
                    .quantity(item.getQuantity())
                    .build();
            orders.getOrderItems().add(savedItem);
        }

        return new OrderResponse(orders);
    }

    public OrderResponse getOrder(String publicId, Long orderId) {
        Member member = memberRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        if (!orders.getMember().getMemberId().equals(member.getMemberId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        return new OrderResponse(orders);
    }

    public List<OrderResponse> getMyOrders(String publicId) {
        Member member = memberRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        return orderRepository.findAllByMemberIdWithItems(member.getMemberId())
                .stream()
                .map(OrderResponse::new)
                .toList();
    }

    @Transactional
    public OrderResponse cancelOrder(String publicId, Long orderId) {
        Member member = memberRepository.findByPublicId(publicId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));

        if (!orders.getMember().getMemberId().equals(member.getMemberId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }

        orders.cancel();

        // 재고 복구
        for (OrderItem item : orders.getOrderItems()) {
            stockLockService.increaseWithLock(item.getProduct().getProductId(), item.getQuantity());
        }

        return new OrderResponse(orders);
    }

    private String generateOrderNumber() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "ORD-" + date + "-" + uuid;
    }
}
