package com.example.demo.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class LlmService {

    public Map<String, Object> analyzeTextWithGpt(String prompt) {

        String fakeSqlAnswer = "SELECT first_name, last_name, email \n" +
                "FROM users \n" +
                "WHERE created_at >= NOW() - INTERVAL '30 days' \n" +
                "ORDER BY created_at DESC;";

        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("status", "success");
        mockResponse.put("provider", "OpenAI GPT-5.4");
        mockResponse.put("user_prompt", prompt);
        mockResponse.put("intent_analysis", "Request identified as a search for active users over the last month. Generated an optimized query for PostgreSQL DBMS.");
        mockResponse.put("generated_sql", fakeSqlAnswer);
        mockResponse.put("confidence_score", 0.99);

        return mockResponse;
    }
}