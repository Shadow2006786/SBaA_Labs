package com.example.demo.service;

import opennlp.tools.namefind.*;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class OpenNlpService {

    private TokenNameFinderModel trainedNerModel;

    // 1. Використання токенізатора (розбиття тексту на слова)
    public String[] tokenizeText(String text) {
        // Використовуємо вбудований SimpleTokenizer, який не потребує додаткових файлів моделей
        return SimpleTokenizer.INSTANCE.tokenize(text);
    }

    // 2. Тренування моделі NER на власному корпусі
    public void trainModel() throws Exception {
        File trainingFile = new File("src/main/resources/ner-corpus.txt");
        InputStreamFactory in = new MarkableFileInputStreamFactory(trainingFile);
        ObjectStream<String> lineStream = new PlainTextByLineStream(in, StandardCharsets.UTF_8);
        ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);

        // Тренуємо модель знаходити сутність "person"
        this.trainedNerModel = NameFinderME.train("en", "person", sampleStream,
                TrainingParameters.defaultParams(), new TokenNameFinderFactory());

        sampleStream.close();
    }

    // 3. Використання натренованої моделі для пошуку імен
    public List<String> findPersons(String text) throws Exception {
        if (trainedNerModel == null) {
            throw new IllegalStateException("Модель ще не натренована! Спочатку викличте метод тренування.");
        }

        NameFinderME nameFinder = new NameFinderME(trainedNerModel);
        String[] tokens = tokenizeText(text); // Спочатку розбиваємо текст на токени
        Span[] nameSpans = nameFinder.find(tokens); // Знаходимо сутності

        // Конвертуємо знайдені Span (координати) у реальні слова
        List<String> foundNames = new ArrayList<>();
        for (Span span : nameSpans) {
            StringBuilder name = new StringBuilder();
            for (int i = span.getStart(); i < span.getEnd(); i++) {
                name.append(tokens[i]).append(" ");
            }
            foundNames.add(name.toString().trim());
        }

        nameFinder.clearAdaptiveData(); // Очищаємо кеш після пошуку
        return foundNames;
    }
}