package com.example.demo.service.impl;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.MockDataService;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class MockDataServiceImpl implements MockDataService {

//    private final AirportRepository airportRepository;
//    private final FlightRepository flightRepository;
//    private final BookingRepository bookingRepository;
//    private final StopoverRepository stopoverRepository;
//    private final UserRepository userRepository;
//
//    private final Faker faker;
//    private final Random random;
//
//    public MockDataServiceImpl(AirportRepository airportRepository, FlightRepository flightRepository,
//                               BookingRepository bookingRepository, StopoverRepository stopoverRepository,
//                               UserRepository userRepository) {
//        this.airportRepository = airportRepository;
//        this.flightRepository = flightRepository;
//        this.bookingRepository = bookingRepository;
//        this.stopoverRepository = stopoverRepository;
//        this.userRepository = userRepository;
//        this.faker = new Faker();
//        this.random = new Random();
//    }
//
//    @PostConstruct
//    public void init() {
//        generateMockData();
//    }
//
//    @Override
//    public void generateMockData() {
//        // Generare aeroporturi
//        List<Airport> airports = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            airports.add(createAirport());
//        }
//        airportRepository.saveAll(airports);
//
//        // Generare zboruri
//        List<Flight> flights = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            flights.add(createFlight(airports));
//        }
//        flightRepository.saveAll(flights);
//
//        // Generare utilizatori
//        List<User> users = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            users.add(createUser());
//        }
//        userRepository.saveAll(users);
//
//
//        // Generare stopovere
//        List<Stopover> stopovers = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            stopovers.add(createStopover(flights, airports));
//        }
//        stopoverRepository.saveAll(stopovers);
//    }
//
//    private Airport createAirport() {
//        Airport airport = new Airport();
//        airport.setName(faker.company().name());
//        airport.setCountry(faker.address().countryCode());
//        airport.setCity(faker.address().city());
//        return airport;
//    }
//
//    private Flight createFlight(List<Airport> airports) {
//        Flight flight = new Flight();
//        flight.setDepartureAirport(airports.get(random.nextInt(airports.size())));
//        flight.setArrivalAirport(airports.get(random.nextInt(airports.size())));
//        flight.setDepartureTime(LocalDateTime.now().plusDays(random.nextInt(30)));
//        flight.setArrivalTime(flight.getDepartureTime().plusHours(2));
//        return flight;
//    }
//
//    private User createUser() {
//        User user = new User();
//        user.setName(faker.name().fullName());
//        user.setEmail(faker.internet().emailAddress());
//        user.setEmail(faker.phoneNumber().cellPhone());
//        return user;
//    }
//
//
//    private Stopover createStopover(List<Flight> flights, List<Airport> airports) {
//        Stopover stopover = new Stopover();
//        stopover.setFlight(flights.get(random.nextInt(flights.size())));
//        stopover.setAirport(airports.get(random.nextInt(airports.size())));
//        stopover.setStopoverTime(LocalDateTime.now().plusHours(random.nextInt(12)));
//        return stopover;
//    }
//
//    @Override
//    public List<Flight> getFlights() {
//        return flightRepository.findAll();
//    }
}
