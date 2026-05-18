package com.example.nlpapp.dto;
import java.time.LocalDateTime;
import java.util.Map;
public record ClassificationResponseDto(
    String status, String primaryCategory, double confidenceScore, 
    Map<String, Double> allProbabilities, LocalDateTime processedAt
) {}
