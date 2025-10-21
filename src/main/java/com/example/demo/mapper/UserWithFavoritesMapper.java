package com.example.demo.mapper;

import com.example.demo.DTO.UserWithFavoritesDTO;
import com.example.demo.DTO.FlightDTO;
import com.example.demo.model.User;
import com.example.demo.model.Flight;

import java.util.List;
import java.util.stream.Collectors;

public class UserWithFavoritesMapper {

    public static UserWithFavoritesDTO toDto(User user) {
        if (user == null) return null;

        List<FlightDTO> favoriteFlights = user.getFavoriteFlights() != null ?
                user.getFavoriteFlights().stream()
                        .map(FlightMapper::toDto)
                        .collect(Collectors.toList()) :
                null;

        return UserWithFavoritesDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .favoriteFlights(favoriteFlights)
                .build();
    }
}
