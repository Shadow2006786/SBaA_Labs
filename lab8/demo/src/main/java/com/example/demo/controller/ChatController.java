package com.example.demo.controller;

import com.example.demo.entity.ChatMessage;
import com.example.demo.service.DialogueService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final DialogueService dialogueService;

    public ChatController(DialogueService dialogueService) {
        this.dialogueService = dialogueService;
    }

    @PostMapping("/train")
    public String trainModel() {
        try {
            dialogueService.trainIntentModel();
            return "Модель намірів успішно натренована.";
        } catch (Exception e) {
            return "Помилка тренування: " + e.getMessage();
        }
    }

    @GetMapping("/send")
    public Map<String, Object> sendMessage(@RequestParam String message) {
        return dialogueService.processUserRequest(message);
    }

    @GetMapping("/history")
    public List<ChatMessage> getHistory() {
        return dialogueService.getChatHistory();
    }
}