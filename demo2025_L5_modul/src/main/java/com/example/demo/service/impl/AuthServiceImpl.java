package com.example.demo.service.impl;

import com.example.demo.dto.AuthDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;

@Component
public class AuthServiceImpl implements AuthService {
    private final UserRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository ownerRepository, PasswordEncoder passwordEncoder) {
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User login(AuthDTO auth) {
        User user = ownerRepository.findByEmail(auth.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Invalid credentials"));
        if (!passwordEncoder.matches(auth.getPassword(), user.getPassword())) {
            throw new NoSuchElementException("Invalid credentials");
        }
        return user;
    }

    public void register(AuthDTO auth) {
        if (ownerRepository.findByEmail(auth.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setEmail(auth.getEmail());
        user.setPassword(auth.getPassword());
        ownerRepository.save(user);
    }
}
