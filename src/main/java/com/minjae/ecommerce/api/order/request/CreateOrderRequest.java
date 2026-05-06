package com.minjae.ecommerce.api.order.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderRequest {

    @NotNull(message = "배송지는 필수입니다.")
    private Long addressId;

    @NotEmpty(message = "주문 상품은 1개 이상이어야 합니다.")
    private List<OrderItemRequest> items;

    @Getter
    public static class OrderItemRequest {
        @NotNull(message = "상품 ID는 필수입니다.")
        private Long productId;

        @NotNull(message = "수량은 필수입니다.")
        private Integer quantity;
    }
}
