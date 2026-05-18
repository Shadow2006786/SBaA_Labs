package com.example.demo.controller;

import com.example.demo.entity.Author;
import com.example.demo.service.LibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final LibraryService libraryService;

    // Звичайний запит: http://localhost:8080/api/authors
    // Демонструє проблему N+1
    @GetMapping
    public List<Author> getStandard() {
        return libraryService.getAuthorsStandard();
    }

    // Оптимізований запит: http://localhost:8080/api/authors/optimized
    // Показує вирішення проблеми
    @GetMapping("/optimized")
    public List<Author> getOptimized() {
        return libraryService.getAuthorsOptimized();
    }
}