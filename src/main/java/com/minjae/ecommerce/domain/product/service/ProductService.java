package com.minjae.ecommerce.domain.product.service;

import com.minjae.ecommerce.api.product.request.CreateProductRequest;
import com.minjae.ecommerce.api.product.request.UpdateProductRequest;
import com.minjae.ecommerce.api.product.response.ProductResponse;
import com.minjae.ecommerce.domain.product.entity.Category;
import com.minjae.ecommerce.domain.product.entity.Product;
import com.minjae.ecommerce.domain.product.entity.Stock;
import com.minjae.ecommerce.domain.product.repository.CategoryRepository;
import com.minjae.ecommerce.domain.product.repository.ProductRepository;
import com.minjae.ecommerce.domain.product.repository.StockRepository;
import com.minjae.ecommerce.global.exception.BusinessException;
import com.minjae.ecommerce.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StockRepository stockRepository;

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        Product product = Product.builder()
                .category(category)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .thumbnailUrl(request.getThumbnailUrl())
                .build();

        productRepository.save(product);

        Stock stock = Stock.builder()
                .product(product)
                .quantity(request.getInitialStock())
                .build();

        stockRepository.save(stock);

        return new ProductResponse(product);
    }

    public ProductResponse getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
        return new ProductResponse(product);
    }

    public Page<ProductResponse> getProducts(Long categoryId, String status,
                                             Integer minPrice, Integer maxPrice,
                                             Pageable pageable) {
        return productRepository.searchProducts(categoryId, status, minPrice, maxPrice, pageable)
                .map(ProductResponse::new);
    }

    @Transactional
    public ProductResponse updateProduct(Long productId, UpdateProductRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));

        product.updateProduct(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getThumbnailUrl()
        );

        return new ProductResponse(product);
    }

    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
        product.deactivate();
    }
}
