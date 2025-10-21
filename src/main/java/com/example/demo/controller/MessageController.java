package com.example.demo.controller;

import com.example.demo.model.ChatMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {

    private static final List<ChatMessage> messageHistory = new ArrayList<>(); // Stocare mesaje

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage handleMessage(ChatMessage message) {
        message.setTimestamp(new SimpleDateFormat("HH:mm:ss").format(new Date()));
        messageHistory.add(message); // Adaugă la istoric
        return message;
    }

    @GetMapping("/chat/history") // Endpoint pentru istoric
    public ResponseEntity<List<ChatMessage>> getHistory() {
        return ResponseEntity.ok(messageHistory);
    }
}