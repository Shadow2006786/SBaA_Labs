package com.example.demo.controller;

import com.example.demo.service.NerService;
import com.example.demo.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/info")
@RequiredArgsConstructor
public class InformationController {

    private final NerService nerService;
    private final SearchService searchService;

    // Ендпоінт для розпізнавання сутностей (NER)
    @GetMapping("/ner")
    public Map<String, List<String>> extractEntities(@RequestParam String text) {
        return nerService.extractEntities(text);
    }

    // Ендпоінт для індексації нового документа
    @PostMapping("/search/index")
    public String indexText(@RequestBody String text) {
        int id = searchService.indexDocument(text);
        return "Документ успішно проіндексовано з ID: " + id;
    }

    // Ендпоінт для пошуку за ключовими словами
    @GetMapping("/search")
    public List<Map<String, Object>> searchByKeywords(@RequestParam String query) {
        return searchService.search(query);
    }
}