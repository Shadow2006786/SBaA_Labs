package com.example.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SentimentServiceTest {

    @InjectMocks
    private SentimentService sentimentService;

    @Test
    public void testRuleBasedAnalysis_PositiveSentiment() {
        String text = "The new smartphone is completely amazing and very fast.";

        Map<String, Object> result = sentimentService.ruleBasedAnalysis(text);

        assertNotNull(result);
        assertEquals("Positive", result.get("sentiment"));
        assertTrue((int) result.get("score") > 0);
    }

    @Test
    public void testRuleBasedAnalysis_NegativeSentiment() {
        String text = "This battery is terrible and broken.";
        Map<String, Object> result = sentimentService.ruleBasedAnalysis(text);
        assertEquals("Negative", result.get("sentiment"));
    }
}