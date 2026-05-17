package com.example.demo.controller;

import com.example.demo.service.DocumentClassifierService;
import com.example.demo.service.TextProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class TextAnalysisController {

    private final TextProcessingService textProcessor;
    private final DocumentClassifierService classifierService;

    // Ендпоінт: Пайплайн обробки та візуалізація змін
    @GetMapping("/pipeline")
    public Map<String, Object> runPipeline(@RequestParam String text) {
        return textProcessor.processTextPipeline(text);
    }

    // Ендпоінт: Навчання класифікатора
    @PostMapping("/train-classifier")
    public String trainClassifier() {
        try {
            classifierService.trainCategorizer();
            return "Модель класифікації успішно натренована на корпусі!";
        } catch (Exception e) {
            return "Помилка тренування: " + e.getMessage();
        }
    }

    // Ендпоінт: Оцінка та класифікація
    @GetMapping("/categorize")
    public Map<String, Object> categorize(@RequestParam String text) {
        return classifierService.categorizeText(text);
    }
}