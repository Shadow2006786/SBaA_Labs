package com.example.demo.service;

import opennlp.tools.doccat.*;
import opennlp.tools.util.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class DocumentClassifierService {

    private DoccatModel categoryModel;
    private final TextProcessingService textProcessor;

    public DocumentClassifierService(TextProcessingService textProcessor) {
        this.textProcessor = textProcessor;
    }

    // Навчання моделі на прикладах категорій
    public void trainCategorizer() throws Exception {
        File trainingFile = new File("src/main/resources/doc-category-corpus.txt");
        InputStreamFactory in = new MarkableFileInputStreamFactory(trainingFile);
        ObjectStream<String> lineStream = new PlainTextByLineStream(in, StandardCharsets.UTF_8);
        ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

        // Тренуємо модель DocumentCategorizer
        this.categoryModel = DocumentCategorizerME.train("en", sampleStream,
                TrainingParameters.defaultParams(), new DoccatFactory());

        sampleStream.close();
    }

    // Класифікація тексту та оцінка точності (ймовірності)
    public Map<String, Object> categorizeText(String text) {
        if (categoryModel == null) {
            throw new IllegalStateException("Модель категорій не натренована!");
        }

        DocumentCategorizerME categorizer = new DocumentCategorizerME(categoryModel);

        // Використовуємо наш пайплайн для підготовки тексту перед класифікацією
        String[] processedTokens = textProcessor.getProcessedTokens(text);

        // Отримуємо масив ймовірностей для кожної категорії
        double[] probabilities = categorizer.categorize(processedTokens);

        // Визначаємо найкращу категорію
        String bestCategory = categorizer.getBestCategory(probabilities);

        Map<String, Object> result = new HashMap<>();
        result.put("text", text);
        result.put("predicted_category", bestCategory);
        result.put("accuracy_score", categorizer.getAllResults(probabilities)); // Показує % впевненості моделі

        return result;
    }
}