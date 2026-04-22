package com.minjae.ecommerce.domain.product.repository;

import com.minjae.ecommerce.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    // 카테고리 + 상태 + 가격 범위 조건 검색
    Page<Product> searchProducts(Long categoryId, String status, Integer minPrice, Integer maxPrice, Pageable pageable);
}
