package com.example.demo.service.decorator;

import com.example.demo.model.Flight;
import com.example.demo.model.User;
import com.example.demo.service.FlightService;

import java.util.List;

public class FlightServiceLoggingDecorator implements FlightService {

    private final FlightService decorated;

    public FlightServiceLoggingDecorator(FlightService decorated) {
        this.decorated = decorated;
    }

    @Override
    public List<Flight> getAllFlights() {
        System.out.println("[LOG] Fetching all flights");
        return decorated.getAllFlights();
    }

    @Override
    public Flight getFlightById(Long id) {
        System.out.println("[LOG] Fetching flight by id: " + id);
        return decorated.getFlightById(id);
    }

    @Override
    public Flight addFlight(Flight flight) {
        System.out.println("[LOG] Adding flight: " + flight);
        Flight result = decorated.addFlight(flight);
        System.out.println("[LOG] Flight added with id: " + result.getId());
        return result;
    }

    @Override
    public Flight updateFlight(Flight flight) {
        System.out.println("[LOG] Updating flight with id: " + flight.getId());
        return decorated.updateFlight(flight);
    }

    @Override
    public void deleteFlight(Long id) {
        System.out.println("[LOG] Deleting flight with id: " + id);
        decorated.deleteFlight(id);
        System.out.println("[LOG] Flight deleted with id: " + id);
    }

    @Override
    public List<Flight> findFlightsByCities(String fromCity, String toCity) {
        System.out.println("[LOG] Searching flights from " + fromCity + " to " + toCity);
        return decorated.findFlightsByCities(fromCity, toCity);
    }

    @Override
    public List<Flight> getFavoriteFlights(User user) {
        System.out.println("[LOG] Getting favorite flights for user: " + user);
        return decorated.getFavoriteFlights(user);
    }

    @Override
    public List<Flight> findFlightsByMaxPrice(double maxPrice) {
        System.out.println("[LOG] Searching flights with max price: " + maxPrice);
        List<Flight> flights = decorated.findFlightsByMaxPrice(maxPrice);
        System.out.println("[LOG] Found " + flights.size() + " flights with price <= " + maxPrice);
        return flights;
    }
}
