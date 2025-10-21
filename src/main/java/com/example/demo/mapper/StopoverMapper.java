package com.example.demo.mapper;

import com.example.demo.DTO.StopoverDTO;
import com.example.demo.model.Airport;
import com.example.demo.model.Flight;
import com.example.demo.model.Stopover;

public class StopoverMapper {

    // Entity -> DTO
    public static StopoverDTO toDto(Stopover stopover) {
        if (stopover == null) return null;

        return StopoverDTO.builder()
                .id(stopover.getId())
                .flightId(stopover.getFlight() != null ? stopover.getFlight().getId() : null)
                .airport(AirportMapper.toDto(stopover.getAirport()))
                .stopoverTime(stopover.getStopoverTime())
                .build();
    }

    // DTO -> Entity
    public static Stopover toEntity(
            StopoverDTO dto,
            Flight flight,
            Airport airport
    ) {
        if (dto == null) return null;

        return Stopover.builder()
                .id(dto.getId())
                .flight(flight)
                .airport(airport)
                .stopoverTime(dto.getStopoverTime())
                .build();
    }
}
