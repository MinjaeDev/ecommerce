package com.minjae.ecommerce.domain.product.repository;

import com.minjae.ecommerce.domain.product.entity.Product;
import com.minjae.ecommerce.domain.product.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    boolean existsByNameAndStatus(String name, ProductStatus status);
}
