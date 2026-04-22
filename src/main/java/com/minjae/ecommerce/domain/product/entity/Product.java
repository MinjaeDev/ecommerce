package com.minjae.ecommerce.domain.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 200)
    private String name;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Column(length = 500)
    private String thumbnailUrl;

    // Elasticsearch 문서 ID 연동용
    @Column(length = 100)
    private String esDocId;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Stock stock;

    @Builder
    public Product(Category category, String name, String description,
                   BigDecimal price, String thumbnailUrl) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        this.thumbnailUrl = thumbnailUrl;
        this.status = ProductStatus.ON_SALE;
    }

    // 비즈니스 메서드
    public void updateProduct(String name, String description,
                              BigDecimal price, String thumbnailUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.thumbnailUrl = thumbnailUrl;
    }

    public void updateEsDocId(String esDocId) {
        this.esDocId = esDocId;
    }

    public void deactivate() {
        this.status = ProductStatus.INACTIVE;
    }

    public void markSoldOut() {
        this.status = ProductStatus.SOLD_OUT;
    }

    public void markOnSale() {
        this.status = ProductStatus.ON_SALE;
    }
}
