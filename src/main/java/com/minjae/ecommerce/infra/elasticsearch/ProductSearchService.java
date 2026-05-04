package com.minjae.ecommerce.infra.elasticsearch;

import com.minjae.ecommerce.infra.ollama.EmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductSearchRepository productSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;
    private final EmbeddingService embeddingService;

    //상품 ES 저장
    public ProductDocument indexProduct(ProductDocument document) {
        return productSearchRepository.save(document);
    }

    // Hybrid Search(BM25 + kNN)
    public List<ProductDocument> hybridSearch(String keyword) {
        // 검색어 Embedding 생성
        List<Float> queryVector = embeddingService.generateEmbedding(keyword);

        //kNN 백터 검색 쿼리
        NativeQuery query = NativeQuery.builder()
                .withKnnSearches(knn -> knn
                        .field("embeddingVector")
                        .queryVector(queryVector)
                        .numCandidates(50)
                        .k(10)
                )
                .withQuery(q -> q
                        .multiMatch(mm -> mm
                                .fields("name", "description")
                                .query(keyword)))
                .build();

        SearchHits<ProductDocument> hits = elasticsearchOperations.search(query, ProductDocument.class);

        return hits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .toList();
    }

    //상품 삭제 시 ES에서도 제거
    public void deleteProduct(String esDocId) {
        productSearchRepository.deleteById(esDocId);
    }
}
