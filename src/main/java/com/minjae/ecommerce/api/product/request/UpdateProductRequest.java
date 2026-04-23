package com.minjae.ecommerce.api.product.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class UpdateProductRequest {

    @NotBlank(message = "상품명은 필수입니다.")
    @Size(max = 200)
    private String name;

    @NotBlank(message = "상품 설명은 필수입니다.")
    private String description;

    @NotNull(message = "가격은 필수입니다.")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal price;

    private String thumbnailUrl;
}
