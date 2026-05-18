package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userRequest;
    private String botResponse;
    private String intent;
    private LocalDateTime timestamp;

    public ChatMessage() {
        this.timestamp = LocalDateTime.now();
    }

    public ChatMessage(String userRequest, String botResponse, String intent) {
        this.userRequest = userRequest;
        this.botResponse = botResponse;
        this.intent = intent;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getUserRequest() { return userRequest; }
    public String getBotResponse() { return botResponse; }
    public String getIntent() { return intent; }
    public LocalDateTime getTimestamp() { return timestamp; }
}