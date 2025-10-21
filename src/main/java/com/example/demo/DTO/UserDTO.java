package com.example.demo.DTO;


import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.example.demo.validation.annotations.UniqueEmail;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    @UniqueEmail
    private String email;
    @NotBlank
    private String role;
}