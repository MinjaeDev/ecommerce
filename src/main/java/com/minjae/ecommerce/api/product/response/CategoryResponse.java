package com.minjae.ecommerce.api.product.response;

import com.minjae.ecommerce.domain.product.entity.Category;
import lombok.Getter;

import java.util.List;

@Getter
public class CategoryResponse {

    private final Long categoryId;
    private final String name;
    private final int depth;
    private final int sortOrder;
    private final List<CategoryResponse> children;

    public CategoryResponse(Category category) {
        this.categoryId = category.getCategoryId();
        this.name = category.getName();
        this.depth = category.getDepth();
        this.sortOrder = category.getSortOrder();
        this.children = category.getChildren().stream()
                .map(CategoryResponse::new)
                .toList();
    }
}
