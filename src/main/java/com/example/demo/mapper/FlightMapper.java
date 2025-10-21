package com.example.demo.mapper;

import com.example.demo.DTO.FlightDTO;
import com.example.demo.model.Airport;
import com.example.demo.model.Flight;

public class FlightMapper {

    public static FlightDTO toDto(Flight flight) {
        if (flight == null) return null;

        return FlightDTO.builder()
                .id(flight.getId())
                .airline(flight.getAirline())
                .departureAirport(AirportMapper.toDto(flight.getDepartureAirport()))
                .arrivalAirport(AirportMapper.toDto(flight.getArrivalAirport()))
                .flightDuration(flight.getFlightDuration())
                .departureTime(flight.getDepartureTime())
                .arrivalTime(flight.getArrivalTime())
                .totalSeats(flight.getTotalSeats())
                .pricePerSeat(flight.getPricePerSeat())
                .build();
    }

    public static Flight toNewEntity(FlightDTO dto) {
        if (dto == null) return null;

        return Flight.builder()
                .airline(dto.getAirline())
                .departureAirport(Airport.builder().id(dto.getDepartureAirport().getId()).build())
                .arrivalAirport(Airport.builder().id(dto.getArrivalAirport().getId()).build())
                .flightDuration(dto.getFlightDuration())
                .departureTime(dto.getDepartureTime())
                .arrivalTime(dto.getArrivalTime())
                .totalSeats(dto.getTotalSeats())
                .pricePerSeat(dto.getPricePerSeat())
                .build();
    }
}