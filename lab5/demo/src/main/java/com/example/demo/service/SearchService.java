package com.example.demo.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SearchService {

    // Сховище документів: ID -> Текст
    private final Map<Integer, String> documents = new HashMap<>();
    
    // Індекс: Слово -> (ID документа -> Кількість входжень)
    private final Map<String, Map<Integer, Integer>> invertedIndex = new HashMap<>();
    private int docIdCounter = 1;

    // 1. Додавання та індексація тексту
    public int indexDocument(String text) {
        int docId = docIdCounter++;
        documents.put(docId, text);

        // Нормалізація та розбиття на слова
        String[] words = text.toLowerCase().replaceAll("[^a-z0-9\\s]", "").split("\\s+");
        
        for (String word : words) {
            if (word.isEmpty()) continue;
            invertedIndex.putIfAbsent(word, new HashMap<>());
            Map<Integer, Integer> docFrequency = invertedIndex.get(word);
            docFrequency.put(docId, docFrequency.getOrDefault(docId, 0) + 1);
        }
        return docId;
    }

    // 2. Пошук та визначення релевантності
    public List<Map<String, Object>> search(String query) {
        String[] queryWords = query.toLowerCase().replaceAll("[^a-z0-9\\s]", "").split("\\s+");
        Map<Integer, Integer> documentScores = new HashMap<>();

        // Розраховуємо релевантність (сума частот збігів ключових слів)
        for (String word : queryWords) {
            if (invertedIndex.containsKey(word)) {
                Map<Integer, Integer> docFreq = invertedIndex.get(word);
                for (Map.Entry<Integer, Integer> entry : docFreq.entrySet()) {
                    int docId = entry.getKey();
                    int freq = entry.getValue();
                    documentScores.put(docId, documentScores.getOrDefault(docId, 0) + freq);
                }
            }
        }

        // Сортуємо результати за релевантністю (від найвищого score)
        List<Map<String, Object>> results = new ArrayList<>();
        documentScores.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .forEach(entry -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("docId", entry.getKey());
                    result.put("relevance_score", entry.getValue());
                    result.put("text", documents.get(entry.getKey()));
                    results.add(result);
                });

        return results;
    }
}