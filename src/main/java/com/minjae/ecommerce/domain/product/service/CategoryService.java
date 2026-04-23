package com.minjae.ecommerce.domain.product.service;

import com.minjae.ecommerce.api.product.response.CategoryResponse;
import com.minjae.ecommerce.domain.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAllByParentIsNull()
                .stream()
                .map(CategoryResponse::new)
                .toList();
    }
}
