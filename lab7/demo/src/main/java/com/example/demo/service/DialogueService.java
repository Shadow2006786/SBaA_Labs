package com.example.demo.service;

import com.example.demo.entity.ChatMessage;
import com.example.demo.repository.ChatMessageRepository;
import opennlp.tools.doccat.*;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DialogueService {

    private final ChatMessageRepository chatRepository;
    private DoccatModel intentModel;

    public DialogueService(ChatMessageRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    public void trainIntentModel() throws Exception {
        File trainingFile = new File("src/main/resources/intent-corpus.txt");
        InputStreamFactory in = new MarkableFileInputStreamFactory(trainingFile);
        ObjectStream<String> lineStream = new PlainTextByLineStream(in, StandardCharsets.UTF_8);
        ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

        // Створюємо власні параметри та вимикаємо ігнорування рідкісних слів
        TrainingParameters params = new TrainingParameters();
        params.put(TrainingParameters.ITERATIONS_PARAM, 100);
        params.put(TrainingParameters.CUTOFF_PARAM, 0);

        // Передаємо наші params замість defaultParams()
        this.intentModel = DocumentCategorizerME.train("en", sampleStream, params, new DoccatFactory());

        sampleStream.close();
    }

    public Map<String, Object> processUserRequest(String text) {
        if (intentModel == null) {
            throw new IllegalStateException("Модель намірів не натренована!");
        }

        DocumentCategorizerME categorizer = new DocumentCategorizerME(intentModel);
        String[] tokens = SimpleTokenizer.INSTANCE.tokenize(text.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase());

        double[] probabilities = categorizer.categorize(tokens);
        String predictedIntent = categorizer.getBestCategory(probabilities);

        String botResponse = generateResponse(predictedIntent);

        ChatMessage savedMessage = chatRepository.save(new ChatMessage(text, botResponse, predictedIntent));

        Map<String, Object> response = new HashMap<>();
        response.put("request", text);
        response.put("detected_intent", predictedIntent);
        response.put("bot_reply", botResponse);
        response.put("db_id", savedMessage.getId());

        return response;
    }

    private String generateResponse(String intent) {
        switch (intent) {
            case "GREETING": return "Hello! How can I help you today?";
            case "WEATHER": return "The weather looks great! However, I am just a text bot without internet access.";
            case "HELP": return "I am an AI assistant. I can classify text, extract entities, and chat with you.";
            case "FAREWELL": return "Goodbye! Have a wonderful day.";
            default: return "I am not sure how to respond to that.";
        }
    }

    public List<ChatMessage> getChatHistory() {
        return chatRepository.findAll();
    }
}