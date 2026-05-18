package com.example.nlpapp.controller;
import com.example.nlpapp.dto.TextRequestDto;
import com.example.nlpapp.dto.ClassificationResponseDto;
import com.example.nlpapp.service.ClassificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/nlp")
public class TextClassificationController {
    private final ClassificationService classificationService;
    public TextClassificationController(ClassificationService classificationService) {
        this.classificationService = classificationService;
    }
    @PostMapping("/classify")
    public ResponseEntity<?> classify(@RequestBody TextRequestDto requestDto) {
        if (requestDto.text() == null || requestDto.text().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Поле 'text' є обов'язковим."));
        }
        ClassificationResponseDto result = classificationService.classifyText(requestDto);
        return ResponseEntity.ok(result);
    }
}
