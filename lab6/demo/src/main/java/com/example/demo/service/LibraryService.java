package com.example.demo.service;

import com.example.demo.entity.Author;
import com.example.demo.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryService {
    private final AuthorRepository authorRepository;

    // Метод, який генерує проблему N+1
    public List<Author> getAuthorsStandard() {
        return authorRepository.findAllStandard();
    }

    // Оптимізований метод, який вирішує N+1
    public List<Author> getAuthorsOptimized() {
        return authorRepository.findAllOptimized();
    }
}