package com.example.demo.DTO;

import com.example.demo.validation.annotations.ConsistentFlightTimes;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ConsistentFlightTimes
public class FlightDTO {
    private Long id;

    @NotBlank(message = "Airline name is required")
    @Size(max = 100, message = "Airline name must be less than 100 characters")
    private String airline;

    @NotNull(message = "Departure airport is required")
    private AirportDTO departureAirport;

    @NotNull(message = "Arrival airport is required")
    private AirportDTO arrivalAirport;

    @Min(value = 1, message = "Flight duration must be at least 1 minute")
    private int flightDuration;

    @NotNull(message = "Departure time is required")
    private LocalDateTime departureTime;

    @NotNull(message = "Arrival time is required")
    private LocalDateTime arrivalTime;

    @Min(value = 1, message = "Total seats must be at least 1")
    private int totalSeats;

    @DecimalMin(value = "0.0", message = "Price per seat must be positive")
    private double pricePerSeat;
}