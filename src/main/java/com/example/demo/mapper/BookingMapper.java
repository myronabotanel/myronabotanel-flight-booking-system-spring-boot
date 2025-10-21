package com.example.demo.mapper;

import com.example.demo.DTO.BookingDTO;
import com.example.demo.model.Airport;
import com.example.demo.model.Booking;
import com.example.demo.model.Flight;
import com.example.demo.model.User;

public class BookingMapper {

    // Entity -> DTO
    public static BookingDTO toDto(Booking booking) {
        if (booking == null) return null;

        return BookingDTO.builder()
                .id(booking.getId())
                .userId(booking.getUser() != null ? booking.getUser().getId() : null)
                .flightId(booking.getFlight() != null ? booking.getFlight().getId() : null)
                .numberOfSeats(booking.getNumberOfSeats())
                .totalPrice(booking.getTotalPrice())
                .departureAirport(AirportMapper.toDto(booking.getDepartureAirport()))
                .arrivalAirport(AirportMapper.toDto(booking.getArrivalAirport()))
                .build();
    }

    // DTO -> Entity
    public static Booking toEntity(
            BookingDTO dto,
            User user,
            Flight flight,
            Airport departureAirport,
            Airport arrivalAirport
    ) {
        if (dto == null) return null;

        return Booking.builder()
                .id(dto.getId())
                .user(user)
                .flight(flight)
                .numberOfSeats(dto.getNumberOfSeats())
                .totalPrice(dto.getTotalPrice())
                .departureAirport(departureAirport)
                .arrivalAirport(arrivalAirport)
                .build();
    }
}
