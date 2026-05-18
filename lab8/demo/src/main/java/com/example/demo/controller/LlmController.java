package com.example.demo.controller;

import com.example.demo.service.LlmService;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/llm")
public class LlmController {

    private final LlmService llmService;

    public LlmController(LlmService llmService) {
        this.llmService = llmService;
    }

    @GetMapping("/analyze")
    public Map<String, Object> analyzeWithLlm(@RequestParam String text) {
        return llmService.analyzeTextWithGpt(text);
    }
}