package com.example.demo.service;

import com.example.demo.exceptions.ApiExceptionResponse;
import com.example.demo.model.Airport;

import java.util.List;

public interface AirportService {
    Airport addAirport(Airport airport);
    List<Airport> getAllAirports();
    Airport getAirportById(Long id) throws ApiExceptionResponse;
    void deleteAirport(Long id) throws ApiExceptionResponse;
    Airport updateAirport(Airport airport);
}
