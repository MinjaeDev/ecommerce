package com.minjae.ecommerce.api.product.response;

import com.minjae.ecommerce.domain.product.entity.Product;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class ProductResponse {

    private final Long productId;
    private final Long categoryId;
    private final String categoryName;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final String status;
    private final String thumbnailUrl;
    private final int stockQuantity;
    private final LocalDateTime createdAt;

    public ProductResponse(Product product) {
        this.productId = product.getProductId();
        this.categoryId = product.getCategory().getCategoryId();
        this.categoryName = product.getCategory().getName();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.status = product.getStatus().name();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.stockQuantity = product.getStock() != null
                ? product.getStock().getQuantity() : 0;
        this.createdAt = product.getCreatedAt();
    }
}
