package com.example.demo.service;

import com.example.demo.service.decorator.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    private final BookingService bookingServiceImpl;
    private final AirportService airportServiceImpl;
    private final FlightService flightServiceImpl;
    private final StopoverService stopoverServiceImpl;
    private final UserService userServiceImpl;

    public ServiceConfig(
            BookingService bookingServiceImpl,
            AirportService airportServiceImpl,
            FlightService flightServiceImpl,
            StopoverService stopoverServiceImpl,
            UserService userServiceImpl
    ) {
        this.bookingServiceImpl = bookingServiceImpl;
        this.airportServiceImpl = airportServiceImpl;
        this.flightServiceImpl = flightServiceImpl;
        this.stopoverServiceImpl = stopoverServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }

    @Bean
    public BookingService bookingService() {
        return new BookingServiceLoggingDecorator(bookingServiceImpl);
    }

    @Bean
    public AirportService airportService() {
        return new AirportServiceLoggingDecorator(airportServiceImpl);
    }

    @Bean
    public FlightService flightService() {
        return new FlightServiceLoggingDecorator(flightServiceImpl);
    }

    @Bean
    public StopoverService stopoverService() {
        return new StopoverServiceLoggingDecorator(stopoverServiceImpl);
    }

    @Bean
    public UserService userService() {
        return new UserServiceLoggingDecorator(userServiceImpl);
    }
}
