package com.minjae.ecommerce.domain.product.repository;

import com.minjae.ecommerce.domain.product.entity.Stock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProduct_ProductId(Long productId);

    // 낙관적 락 적용 재고 조회
    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT s FROM Stock s WHERE s.product.productId = :productId")
    Optional<Stock> findByProductIdWithOptimisticLock(Long productId);
}
