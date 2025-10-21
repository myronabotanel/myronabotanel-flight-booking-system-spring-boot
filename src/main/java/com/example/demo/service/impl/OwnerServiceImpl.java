package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceImpl {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyOwner(String message) {
        messagingTemplate.convertAndSend("/topic/socket/owner", message);
    }
}