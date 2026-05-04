package com.minjae.ecommerce.infra.ollama;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class EmbeddingService {

    private final RestClient restClient;
    private final String embeddingModel;

    public EmbeddingService(
            @Value("${ollama.base-url}") String baseUrl,
            @Value("${ollama.embedding-model}") String embeddingModel) {

        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
        this.embeddingModel = embeddingModel;
    }

    public List<Float> generateEmbedding(String text) {
        try {
            Map<String, Object> request = Map.of(
                    "model", embeddingModel,
                    "prompt", text
            );

            EmbeddingResponse response = restClient.post()
                    .uri("/api/embeddings")
                    .body(request)
                    .retrieve()
                    .body(EmbeddingResponse.class);

            if (response == null || response.getEmbedding() == null) {
                throw new RuntimeException("Embedding 생성 실패");
            }

            return response.getEmbedding();

        } catch (Exception e) {
            log.error("Embedding 생성 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("Embedding 생성 실패", e);
        }
    }

    // 상품명 + 설명 텍스트 결합
    public List<Float> generateProductEmbedding(String name, String description) {
        String combinedText = name + "\n" + description;
        return generateEmbedding(combinedText);
    }
}
