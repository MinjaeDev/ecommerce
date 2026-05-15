package com.minjae.ecommerce.api.payment.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PaymentCancelRequest {

    @NotBlank(message = "취소 사유는 필수입니다.")
    private String cancelReason;

    @NotNull(message = "취소 금액은 필수입니다.")
    private BigDecimal cancelAmount;
}
