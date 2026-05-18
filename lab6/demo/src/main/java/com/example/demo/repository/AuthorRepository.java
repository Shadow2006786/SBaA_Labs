package com.example.demo.repository;

import com.example.demo.entity.Author;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Стандартний метод (викликає N+1 запитів у консолі)
    @Query("SELECT a FROM Author a")
    List<Author> findAllStandard();

    // Вирішення N+1: завантажує авторів та їхні книги ОДНИМ запитом (JOIN)
    @EntityGraph(attributePaths = {"books"})
    @Query("SELECT a FROM Author a")
    List<Author> findAllOptimized();
}