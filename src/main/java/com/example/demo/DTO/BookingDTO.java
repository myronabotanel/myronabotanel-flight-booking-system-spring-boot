package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDTO {
    private Long id;
    private Long userId;
    private Long flightId;
    private int numberOfSeats;
    private double totalPrice;
    private AirportDTO departureAirport;
    private AirportDTO arrivalAirport;
}
