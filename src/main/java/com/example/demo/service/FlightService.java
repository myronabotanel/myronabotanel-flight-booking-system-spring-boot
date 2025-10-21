package com.example.demo.service;

import com.example.demo.model.Flight;
import com.example.demo.model.User;

import java.util.List;

public interface FlightService {
    List<Flight> getAllFlights();
    Flight getFlightById(Long id);
    Flight addFlight(Flight flight);
    Flight updateFlight(Flight flight);
    void deleteFlight(Long id);
    List<Flight> findFlightsByCities(String fromCity, String toCity);
    List<Flight> getFavoriteFlights(User user);
    List<Flight> findFlightsByMaxPrice(double maxPrice);
}
