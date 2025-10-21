package com.example.demo.validation.validators;

import com.example.demo.DTO.FlightDTO;
import com.example.demo.validation.annotations.ConsistentFlightTimes;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class ConsistentFlightTimesValidator implements ConstraintValidator<ConsistentFlightTimes, FlightDTO> {

    @Override
    public void initialize(ConsistentFlightTimes constraintAnnotation) {
    }

    @Override
    public boolean isValid(FlightDTO flightDTO, ConstraintValidatorContext context) {
        if (flightDTO == null || flightDTO.getDepartureTime() == null || flightDTO.getArrivalTime() == null) {
            // Lăsăm @NotNull să se ocupe de obiectul null sau de câmpurile de dată/oră nule
            return true;
        }

        // Verifică dacă data și ora sosirii sunt după data și ora plecării
        return flightDTO.getArrivalTime().isAfter(flightDTO.getDepartureTime());
    }
} 