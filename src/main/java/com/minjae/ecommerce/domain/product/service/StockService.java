package com.minjae.ecommerce.domain.product.service;

import com.minjae.ecommerce.domain.product.entity.Stock;
import com.minjae.ecommerce.domain.product.repository.StockRepository;
import com.minjae.ecommerce.global.exception.BusinessException;
import com.minjae.ecommerce.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    // REQUIRES_NEW 부모 트랜잭션과 별개로 즉시 커밋
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void decrease(Long productId, Integer quantity) {
        Stock stock = stockRepository.findByProductIdWithOptimisticLock(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        if (!stock.isAvailable(quantity)) {
            throw new BusinessException(ErrorCode.OUT_OF_STOCK);
        }

        stock.decrease(quantity);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void increase(Long productId, Integer quantity) {
        Stock stock = stockRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        stock.increase(quantity);
    }
}
