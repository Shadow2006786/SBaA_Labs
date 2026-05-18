package com.example.nlpapp.service;
import com.example.nlpapp.dto.TextRequestDto;
import com.example.nlpapp.dto.ClassificationResponseDto;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class ClassificationService {
    public ClassificationResponseDto classifyText(TextRequestDto request) {
        String content = request.text() != null ? request.text().toLowerCase() : "";
        String primaryCategory = "GENERAL";
        double confidenceScore = 0.50;
        Map<String, Double> probabilities = new HashMap<>();
        
        if (content.contains("code") || content.contains("docker") || content.contains("software")) {
            primaryCategory = "TECHNOLOGY"; confidenceScore = 0.92;
            probabilities.put("TECHNOLOGY", 0.92); probabilities.put("FINANCE", 0.05); probabilities.put("SPORTS", 0.03);
        } else if (content.contains("football") || content.contains("match") || content.contains("game")) {
            primaryCategory = "SPORTS"; confidenceScore = 0.88;
            probabilities.put("SPORTS", 0.88); probabilities.put("TECHNOLOGY", 0.02); probabilities.put("FINANCE", 0.10);
        } else if (content.contains("money") || content.contains("stocks") || content.contains("bank")) {
            primaryCategory = "FINANCE"; confidenceScore = 0.85;
            probabilities.put("FINANCE", 0.85); probabilities.put("TECHNOLOGY", 0.10); probabilities.put("SPORTS", 0.05);
        } else {
            probabilities.put("GENERAL", 0.60); probabilities.put("TECHNOLOGY", 0.15);
            probabilities.put("FINANCE", 0.15); probabilities.put("SPORTS", 0.10);
        }

        return new ClassificationResponseDto("SUCCESS", primaryCategory, confidenceScore, probabilities, LocalDateTime.now());
    }
}
