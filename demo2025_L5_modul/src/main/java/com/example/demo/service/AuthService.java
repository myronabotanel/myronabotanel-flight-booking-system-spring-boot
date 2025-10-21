package com.example.demo.service;

import com.example.demo.dto.AuthDTO;
import com.example.demo.model.User;


public interface AuthService {

    User login(AuthDTO auth);
    void register(AuthDTO auth);
}
