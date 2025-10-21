package com.example.demo.validation.annotations;

import com.example.demo.validation.validators.ConsistentFlightTimesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConsistentFlightTimesValidator.class)
@Documented
public @interface ConsistentFlightTimes {

    String message() default "Arrival time must be after departure time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    // Atribute pentru a specifica câmpurile de comparat (opțional, dar bun pentru reutilizare)
    String departureTime() default "departureTime";
    String arrivalTime() default "arrivalTime";
} 