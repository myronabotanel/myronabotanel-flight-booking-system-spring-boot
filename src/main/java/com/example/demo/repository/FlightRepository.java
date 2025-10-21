package com.example.demo.repository;

import com.example.demo.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByDepartureAirportCityAndArrivalAirportCity(String fromCity, String toCity);
    List<Flight> findByPricePerSeatLessThanEqualOrderByPricePerSeatAsc(double maxPrice);
}
