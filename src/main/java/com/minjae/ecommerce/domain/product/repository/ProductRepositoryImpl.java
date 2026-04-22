package com.minjae.ecommerce.domain.product.repository;

import com.minjae.ecommerce.domain.product.entity.Product;
import com.minjae.ecommerce.domain.product.entity.ProductStatus;
import com.minjae.ecommerce.domain.product.entity.QProduct;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QProduct product = QProduct.product;

    @Override
    public Page<Product> searchProducts(Long categoryId, String status, Integer minPrice, Integer maxPrice, Pageable pageable) {
        List<Product> content = queryFactory
                .selectFrom(product)
                .where(
                        categoryIdEq(categoryId),
                        statusEq(status),
                        minPriceGoe(minPrice),
                        maxPriceLoe(maxPrice)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(product.count())
                .from(product)
                .where(
                        categoryIdEq(categoryId),
                        statusEq(status),
                        minPriceGoe(minPrice),
                        maxPriceLoe(maxPrice)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    private BooleanExpression categoryIdEq(Long categoryId) {
        return categoryId != null ? product.category.categoryId.eq(categoryId) : null;
    }

    private BooleanExpression statusEq(String status) {
        return status != null ? product.status.eq(ProductStatus.valueOf(status)) : null;
    }

    private BooleanExpression minPriceGoe(Integer minPrice) {
        return minPrice != null ? product.price.goe(BigDecimal.valueOf(minPrice)) : null;
    }

    private BooleanExpression maxPriceLoe(Integer maxPrice) {
        return maxPrice != null ? product.price.loe(BigDecimal.valueOf(maxPrice)) : null;
    }
}
