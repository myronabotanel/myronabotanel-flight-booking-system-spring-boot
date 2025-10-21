package com.example.demo.service.impl;

import com.example.demo.exceptions.ApiExceptionResponse;
import com.example.demo.model.Airport;
import com.example.demo.repository.AirportRepository;
import com.example.demo.service.AirportService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;

    public AirportServiceImpl(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    @Override
    public Airport addAirport(Airport airport) {
        return airportRepository.save(airport);
    }

    @Override
    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

    @Override
    public Airport getAirportById(Long id) throws ApiExceptionResponse {
        return airportRepository.findById(id)
                .orElseThrow(() -> ApiExceptionResponse.builder()
                        .status(HttpStatus.NOT_FOUND)
                        .message("Airport with id " + id + " not found")
                        .errors(Collections.singletonList("No airport with id " + id))
                        .build());
    }

    @Override
    public Airport updateAirport(Airport airport) {
        return airportRepository.save(airport);
    }

    @Override
    public void deleteAirport(Long id) throws ApiExceptionResponse {
        if (airportRepository.existsById(id)) {
            airportRepository.deleteById(id);
        } else {
            throw ApiExceptionResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Airport with id " + id + " not found")
                    .errors(Collections.singletonList("No airport with id " + id))
                    .build();
        }
    }
}