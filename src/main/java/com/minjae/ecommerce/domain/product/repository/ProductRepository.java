package com.minjae.ecommerce.domain.product.repository;

import com.minjae.ecommerce.domain.product.entity.Product;
import com.minjae.ecommerce.domain.product.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    boolean existsByNameAndStatus(String name, ProductStatus status);

    @Query("SELECT p FROM Product p JOIN FETCH p.stock WHERE p.productId = :productId")
    Optional<Product> findByIdWithStock(Long productId);
}
