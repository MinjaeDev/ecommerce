package com.minjae.ecommerce.api.payment.request;

import com.minjae.ecommerce.domain.payment.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PaymentRequest {

    @NotNull(message = "주문 Id는 필수입니다.")
    private Long orderId;

    @NotNull(message = "결제 수단은 필수입니다.")
    private PaymentMethod method;
}
