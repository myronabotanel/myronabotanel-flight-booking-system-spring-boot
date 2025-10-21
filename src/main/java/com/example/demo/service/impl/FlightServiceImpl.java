package com.example.demo.service.impl;

import com.example.demo.model.Flight;
import com.example.demo.model.User;
import com.example.demo.model.Airport;
import com.example.demo.repository.FlightRepository;
import com.example.demo.repository.AirportRepository;
import com.example.demo.service.FlightService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final AirportRepository airportRepository;
    private final Random random = new Random();

    @Autowired
    public FlightServiceImpl(FlightRepository flightRepository, AirportRepository airportRepository) {
        this.flightRepository = flightRepository;
        this.airportRepository = airportRepository;
    }

    @Override
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();  // Obține toate zborurile
    }

    @Override
    public Flight getFlightById(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Flight not found with id: " + id));
    }

    @Override
    public Flight addFlight(Flight flight) {
        // Ensure departure and arrival airports are managed entities by fetching them from the database
        // Access IDs safely in case Airport objects are not fully mapped by DTO->Entity
        Long departureAirportId = (flight != null && flight.getDepartureAirport() != null) ? flight.getDepartureAirport().getId() : null;
        Long arrivalAirportId = (flight != null && flight.getArrivalAirport() != null) ? flight.getArrivalAirport().getId() : null;

        if (departureAirportId == null || arrivalAirportId == null) {
            throw new IllegalArgumentException("Departure and arrival airport IDs must not be null.");
        }

        Airport departureAirport = airportRepository.findById(departureAirportId)
                .orElseThrow(() -> new NoSuchElementException("Departure airport not found with id: " + departureAirportId));
        Airport arrivalAirport = airportRepository.findById(arrivalAirportId)
                .orElseThrow(() -> new NoSuchElementException("Arrival airport not found with id: " + arrivalAirportId));

        flight.setDepartureAirport(departureAirport);
        flight.setArrivalAirport(arrivalAirport);

        // Ensure ID is null for new entities - handled by mapper, remove explicit setting here
        // flight.setId(null);

        return flightRepository.save(flight);
    }

    @Override
    public Flight updateFlight(Flight flight) {
        return flightRepository.save(flight);  // Salvează zborul actualizat
    }

    @Override
    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);  // Șterge zborul
    }

    @Override
    public List<Flight> findFlightsByCities(String fromCity, String toCity) {
        return flightRepository.findByDepartureAirportCityAndArrivalAirportCity(fromCity, toCity);  // Căutare zboruri pe baza orașelor
    }

    @Override
    public List<Flight> findFlightsByMaxPrice(double maxPrice) {
        System.out.println("[LOG][FlightService] Searching flights with max price: " + maxPrice);
        List<Flight> flights = flightRepository.findByPricePerSeatLessThanEqualOrderByPricePerSeatAsc(maxPrice);
        System.out.println("[LOG][FlightService] Found " + flights.size() + " flights with price <= " + maxPrice);
        return flights;
    }

    @Override
    public List<Flight> getFavoriteFlights(User user) {
        if (user.getFavoriteFlights() == null || user.getFavoriteFlights().isEmpty()) {
            throw new RuntimeException("No favorite flights found for the user");
        }
        return user.getFavoriteFlights();
    }
}
