package com.minjae.ecommerce.api.product;


import com.minjae.ecommerce.api.product.response.CategoryResponse;
import com.minjae.ecommerce.domain.product.entity.Category;
import com.minjae.ecommerce.domain.product.repository.CategoryRepository;
import com.minjae.ecommerce.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "카테고리", description = "카테고리 조회")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @Operation(summary = "전체 카테고리 조회 (트리 구조)")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories() {
        List<Category> categories = categoryRepository.findAllByParentIsNull();
        return ResponseEntity.ok(ApiResponse.ok(categories.stream()
                .map(CategoryResponse::new)
                .toList()));
    }
}
