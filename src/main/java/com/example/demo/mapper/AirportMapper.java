
package com.example.demo.mapper;

import com.example.demo.DTO.AirportDTO;
import com.example.demo.model.Airport;

import java.util.List;
import java.util.stream.Collectors;

public class AirportMapper {

    // Din DTO -> Entity (folosit când creezi/salvezi un aeroport nou)
    public static Airport toEntity(AirportDTO dto) {
        if (dto == null) return null;

        return Airport.builder()
                .id(dto.getId())
                .name(dto.getName())
                .city(dto.getCity())
                .country(dto.getCountry())
                .build();
    }

    // Din Entity -> DTO (folosit când trimiți un aeroport ca răspuns în API)
    public static AirportDTO toDto(Airport airport) {
        if (airport == null) return null;

        return AirportDTO.builder()
                .id(airport.getId())
                .name(airport.getName())
                .city(airport.getCity())
                .country(airport.getCountry())
                .build();
    }

    // Conversie listă Entity -> listă DTO
    public static List<AirportDTO> toDtoList(List<Airport> airportList) {
        return airportList.stream()
                .map(AirportMapper::toDto)
                .collect(Collectors.toList());
    }

    // Conversie listă DTO -> listă Entity
    public static List<Airport> toEntityList(List<AirportDTO> dtoList) {
        return dtoList.stream()
                .map(AirportMapper::toEntity)
                .collect(Collectors.toList());
    }
}