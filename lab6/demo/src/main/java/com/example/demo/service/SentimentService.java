package com.example.demo.service;

import opennlp.tools.doccat.*;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class SentimentService {

    private DoccatModel sentimentModel;

    // 1. Rule-Based підхід (Словник позитивних та негативних слів)
    private final Set<String> positiveWords = Set.of("amazing", "fast", "love", "best", "excellent", "great", "good");
    private final Set<String> negativeWords = Set.of("terrible", "broken", "hate", "slow", "awful", "worst", "defective", "bad");

    public Map<String, Object> ruleBasedAnalysis(String text) {
        String[] tokens = SimpleTokenizer.INSTANCE.tokenize(text.toLowerCase());
        int score = 0;
        List<String> foundPos = new ArrayList<>();
        List<String> foundNeg = new ArrayList<>();

        for (String token : tokens) {
            if (positiveWords.contains(token)) {
                score++;
                foundPos.add(token);
            } else if (negativeWords.contains(token)) {
                score--;
                foundNeg.add(token);
            }
        }

        String sentiment = score > 0 ? "Positive" : (score < 0 ? "Negative" : "Neutral");

        Map<String, Object> result = new HashMap<>();
        result.put("text", text);
        result.put("sentiment", sentiment);
        result.put("score", score);
        result.put("positive_words_found", foundPos);
        result.put("negative_words_found", foundNeg);
        return result;
    }

    // 2. Навчання ML-моделі для класифікації тональності
    public void trainSentimentModel() throws Exception {
        File trainingFile = new File("src/main/resources/sentiment-corpus.txt");
        InputStreamFactory in = new MarkableFileInputStreamFactory(trainingFile);
        ObjectStream<String> lineStream = new PlainTextByLineStream(in, StandardCharsets.UTF_8);
        ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

        this.sentimentModel = DocumentCategorizerME.train("en", sampleStream,
                TrainingParameters.defaultParams(), new DoccatFactory());
        sampleStream.close();
    }

    // 3. Прогнозування тональності за допомогою ML
    public Map<String, Object> predictSentiment(String text) {
        if (sentimentModel == null) throw new IllegalStateException("Модель тональності не натренована!");

        DocumentCategorizerME categorizer = new DocumentCategorizerME(sentimentModel);
        String[] tokens = SimpleTokenizer.INSTANCE.tokenize(text.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase());
        double[] probabilities = categorizer.categorize(tokens);
        String bestCategory = categorizer.getBestCategory(probabilities);

        Map<String, Object> result = new HashMap<>();
        result.put("text", text);
        result.put("ml_predicted_sentiment", bestCategory);
        result.put("confidence", categorizer.getAllResults(probabilities));
        return result;
    }
}