package com.minjae.ecommerce.api.product;

import com.minjae.ecommerce.global.common.ApiResponse;
import com.minjae.ecommerce.infra.elasticsearch.ProductDocument;
import com.minjae.ecommerce.infra.elasticsearch.ProductSearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "상품 검색", description = "AI 기반 의미론적 상품 검색")
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class ProductSearchController {

    private final ProductSearchService productSearchService;

    @Operation(summary = "AI Hybrid Search (키워드 + 백터 유사도)")
    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductDocument>>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.ok(productSearchService.hybridSearch(keyword)));
    }
}
