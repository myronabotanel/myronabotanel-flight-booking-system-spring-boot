package com.example.demo.mapper;

import com.example.demo.DTO.UserDTO;
import com.example.demo.model.User;

public class UserMapper {

    // Entity -> DTO
    public static UserDTO toDto(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    // DTO -> Entity (doar pentru câmpurile disponibile în DTO)
    public static User toEntity(UserDTO dto) {
        if (dto == null) return null;

        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .role(dto.getRole())
                .build();
    }
}
