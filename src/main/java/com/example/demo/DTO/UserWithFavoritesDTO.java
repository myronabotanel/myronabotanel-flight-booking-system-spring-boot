package com.example.demo.DTO;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWithFavoritesDTO {
    private Long id;
    private String name;
    private String email;
    private List<FlightDTO> favoriteFlights;
}