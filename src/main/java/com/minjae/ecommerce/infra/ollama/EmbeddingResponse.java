package com.minjae.ecommerce.infra.ollama;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class EmbeddingResponse {
    private List<Float> embedding;
}
