package com.example.demo.service;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NerService {

    public Map<String, List<String>> extractEntities(String text) {
        Map<String, List<String>> entities = new HashMap<>();
        String[] tokens = SimpleTokenizer.INSTANCE.tokenize(text);

        entities.put("Persons", find(tokens, "models/en-ner-person.bin"));
        entities.put("Organizations", find(tokens, "models/en-ner-organization.bin"));
        entities.put("Locations", find(tokens, "models/en-ner-location.bin"));

        return entities;
    }

    private List<String> find(String[] tokens, String modelPath) {
        List<String> found = new ArrayList<>();
        try (InputStream modelStream = new ClassPathResource(modelPath).getInputStream()) {
            TokenNameFinderModel model = new TokenNameFinderModel(modelStream);
            NameFinderME nameFinder = new NameFinderME(model);
            
            Span[] spans = nameFinder.find(tokens);
            for (Span span : spans) {
                StringBuilder entity = new StringBuilder();
                for (int i = span.getStart(); i < span.getEnd(); i++) {
                    entity.append(tokens[i]).append(" ");
                }
                found.add(entity.toString().trim());
            }
            nameFinder.clearAdaptiveData();
        } catch (Exception e) {
            System.err.println("Помилка завантаження моделі: " + modelPath);
        }
        return found;
    }
}