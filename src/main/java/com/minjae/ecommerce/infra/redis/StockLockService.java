package com.minjae.ecommerce.infra.redis;

import com.minjae.ecommerce.domain.product.entity.Stock;
import com.minjae.ecommerce.domain.product.repository.StockRepository;
import com.minjae.ecommerce.domain.product.service.StockService;
import com.minjae.ecommerce.global.exception.BusinessException;
import com.minjae.ecommerce.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockLockService {

    private final RedissonClient redissonClient;

    private static final String LOCK_KEY_PREFIX = "stock:lock:";
    private static final long WAIT_TIME = 5L;
    private static final long LEASE_TIME = 10L;

    private final StockService stockService;
    private final StockRepository stockRepository;

//    public void decreaseWithLock(Long productId, int quantity) {
//        RLock lock = redissonClient.getLock(LOCK_KEY_PREFIX + productId);
//        try {
//            boolean acquired = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);
//            if (!acquired) {
//                throw new BusinessException(ErrorCode.OUT_OF_STOCK);
//            }
//            Stock stock = stockRepository.findByProduct_ProductId(productId)
//                    .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
//            if (!stock.isAvailable(quantity)) {
//                throw new BusinessException(ErrorCode.OUT_OF_STOCK);
//            }
//            stock.decrease(quantity);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
//        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//    }
//
//    public void increaseWithLock(Long productId, int quantity) {
//        RLock lock = redissonClient.getLock(LOCK_KEY_PREFIX + productId);
//        try {
//            boolean acquired = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);
//            if (!acquired) {
//                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
//            }
//            Stock stock = stockRepository.findByProduct_ProductId(productId)
//                    .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
//            stock.increase(quantity);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
//        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//    }

    public void decreaseWithLock(Long productId, int quantity) {
        RLock lock = redissonClient.getLock(LOCK_KEY_PREFIX + productId);

        try {
            boolean acquired = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);
            if (!acquired) {
                throw new BusinessException(ErrorCode.OUT_OF_STOCK);
            }

            // REQUIRES_NEW로 락 안에서 커밋까지 완료
            stockService.decrease(productId, quantity);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public void increaseWithLock(Long productId, int quantity) {
        RLock lock = redissonClient.getLock(LOCK_KEY_PREFIX + productId);

        try {
            boolean acquired = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);
            if (!acquired) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
            stockService.increase(productId, quantity);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

//    public <T> T executeWithLock(Long productId, Supplier<T> supplier) {
//        String lockKey = LOCK_KEY_PREFIX + productId;
//        RLock lock = redissonClient.getLock(lockKey);
//
//        try {
//            boolean acquired = lock.tryLock(WAIT_TIME, LEASE_TIME, TimeUnit.SECONDS);
//            if (!acquired) {
//                log.warn("재고 락 획득 실패: productId={}", productId);
//                throw new BusinessException(ErrorCode.OUT_OF_STOCK);
//            }
//            log.debug("재고 락 획득 성공: productId={}", productId);
//            return supplier.get();
//
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
//        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//                log.debug("재고 락 해제: productId={}", productId);
//            }
//        }
//    }
}
