package com.example.demo.controller;

import com.example.demo.service.SemanticService;
import com.example.demo.service.SentimentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/lab6")
@RequiredArgsConstructor
public class TextAnalysisController {

    private final SentimentService sentimentService;
    private final SemanticService semanticService;

    // 1. Rule-Based аналіз
    @GetMapping("/sentiment/rule-based")
    public Map<String, Object> ruleBased(@RequestParam String text) {
        return sentimentService.ruleBasedAnalysis(text);
    }

    // 2. Навчання ML моделі
    @PostMapping("/sentiment/train")
    public String trainSentiment() {
        try {
            sentimentService.trainSentimentModel();
            return "ML модель аналізу тональності успішно натренована!";
        } catch (Exception e) {
            return "Помилка: " + e.getMessage();
        }
    }

    // 3. ML Аналіз (OpenNLP)
    @GetMapping("/sentiment/ml")
    public Map<String, Object> mlSentiment(@RequestParam String text) {
        return sentimentService.predictSentiment(text);
    }

    // 4. Семантична схожість та TF-IDF матриця
    @GetMapping("/semantic/similarity")
    public Map<String, Object> getSimilarity(@RequestParam String text1, @RequestParam String text2) {
        return semanticService.calculateSimilarity(text1, text2);
    }
}