package com.minjae.ecommerce.domain.product.repository;

import com.minjae.ecommerce.domain.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByParentIsNull(); // 최상위 카테고리 조회
    List<Category> findAllByParent_CategoryId(Long parentId);   // 하위 카테고리 조회
}
