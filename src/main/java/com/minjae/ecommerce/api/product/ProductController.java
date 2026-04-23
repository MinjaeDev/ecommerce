package com.minjae.ecommerce.api.product;

import com.minjae.ecommerce.api.product.request.CreateProductRequest;
import com.minjae.ecommerce.api.product.request.UpdateProductRequest;
import com.minjae.ecommerce.api.product.response.ProductResponse;
import com.minjae.ecommerce.domain.product.service.ProductService;
import com.minjae.ecommerce.global.common.ApiResponse;
import com.minjae.ecommerce.global.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "상품", description = "상품 관리")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 등록", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(productService.createProduct(request)));
    }

    @Operation(summary = "상품 단건 조회")
    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(ApiResponse.ok(productService.getProduct(productId)));
    }

    @Operation(summary = "상품 목록 조회 (필터링 + 페이징)")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        Page<ProductResponse> page = productService.getProducts(categoryId, status, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(ApiResponse.ok(new PageResponse<>(page)));
    }

    @Operation(summary = "상품 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(productId, request));
    }

    @Operation(summary = "상품 삭제 (비활성화)", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
