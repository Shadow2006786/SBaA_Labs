package com.example.demo.controller;

import com.example.demo.service.OpenNlpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nlp")
@RequiredArgsConstructor
public class OpenNlpController {

    private final OpenNlpService openNlpService;

    // Ендпоінт для перевірки роботи токенізатора
    @GetMapping("/tokenize")
    public String[] tokenize(@RequestParam String text) {
        return openNlpService.tokenizeText(text);
    }

    // Ендпоінт для запуску навчання моделі
    @PostMapping("/train")
    public String trainModel() {
        try {
            openNlpService.trainModel();
            return "Модель NER успішно натренована на власному корпусі!";
        } catch (Exception e) {
            return "Помилка тренування: " + e.getMessage();
        }
    }

    // Ендпоінт для пошуку імен за допомогою натренованої моделі
    @GetMapping("/extract-persons")
    public List<String> extractPersons(@RequestParam String text) {
        try {
            return openNlpService.findPersons(text);
        } catch (Exception e) {
            return List.of("Помилка: " + e.getMessage());
        }
    }
}