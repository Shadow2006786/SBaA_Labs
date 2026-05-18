package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SemanticService {

    // Побудова TF-IDF матриці та розрахунок косинусної схожості двох текстів
    public Map<String, Object> calculateSimilarity(String text1, String text2) {
        List<String> doc1 = tokenizeAndClean(text1);
        List<String> doc2 = tokenizeAndClean(text2);

        // Формуємо загальний словник унікальних слів з обох текстів
        Set<String> vocabulary = new HashSet<>();
        vocabulary.addAll(doc1);
        vocabulary.addAll(doc2);

        // Розраховуємо вектори частот (спрощений TF)
        List<Double> vector1 = new ArrayList<>();
        List<Double> vector2 = new ArrayList<>();

        for (String word : vocabulary) {
            vector1.add((double) Collections.frequency(doc1, word));
            vector2.add((double) Collections.frequency(doc2, word));
        }

        // Розраховуємо косинусну схожість (Cosine Similarity)
        double similarity = cosineSimilarity(vector1, vector2);

        Map<String, Object> result = new HashMap<>();
        result.put("document_1", text1);
        result.put("document_2", text2);
        result.put("vocabulary", vocabulary);
        result.put("vector_1", vector1);
        result.put("vector_2", vector2);
        result.put("similarity_percentage", Math.round(similarity * 100.0) + "%");

        return result;
    }

    private List<String> tokenizeAndClean(String text) {
        String cleaned = text.toLowerCase().replaceAll("[^a-z\\s]", "");
        return Arrays.asList(cleaned.split("\\s+"));
    }

    private double cosineSimilarity(List<Double> v1, List<Double> v2) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < v1.size(); i++) {
            dotProduct += v1.get(i) * v2.get(i);
            normA += Math.pow(v1.get(i), 2);
            normB += Math.pow(v2.get(i), 2);
        }
        if (normA == 0 || normB == 0) return 0.0;
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}