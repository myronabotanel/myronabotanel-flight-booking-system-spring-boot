package com.example.demo.service.impl;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;

import com.example.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class MockOwnerDataServiceImpl {
    private final Faker faker = new Faker();
    private final Random random = new Random();
    private final UserRepository userRepository;


    public MockOwnerDataServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        generateMockOwnerData();
    }

    public void generateMockOwnerData() {

    }
}

