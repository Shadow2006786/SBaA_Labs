package com.example.demo.service;

import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TextProcessingService {

    // Пайплайн обробки тексту
    public Map<String, Object> processTextPipeline(String originalText) {
        Map<String, Object> results = new HashMap<>();
        results.put("1_original", originalText);

        // 1. Нормалізація (видалення пунктуації та переведення в нижній регістр)
        String normalized = originalText.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
        results.put("2_normalized", normalized);

        // 2. Токенізація
        String[] tokens = SimpleTokenizer.INSTANCE.tokenize(normalized);
        results.put("3_tokens", tokens);

        // 3. Стемінг (зведення слів до кореня: "running" -> "run")
        PorterStemmer stemmer = new PorterStemmer();
        List<String> stemmed = Arrays.stream(tokens)
                .map(stemmer::stem)
                .collect(Collectors.toList());
        results.put("4_stemmed", stemmed);

        return results;
    }

    // Допоміжний метод для отримання лише стемінгованих токенів (для класифікатора)
    public String[] getProcessedTokens(String text) {
        String normalized = text.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
        return SimpleTokenizer.INSTANCE.tokenize(normalized);
    }
}