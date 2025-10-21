package com.example.demo.service.decorator;

import com.example.demo.exceptions.ApiExceptionResponse;
import com.example.demo.model.Airport;
import com.example.demo.service.AirportService;

import java.util.List;

public class AirportServiceLoggingDecorator implements AirportService {

    private final AirportService decorated;

    public AirportServiceLoggingDecorator(AirportService decorated) {
        this.decorated = decorated;
    }

    @Override
    public Airport addAirport(Airport airport) {
        System.out.println("[LOG] Adding airport: " + airport.getName());
        Airport result = decorated.addAirport(airport);
        System.out.println("[LOG] Airport added with id: " + result.getId());
        return result;
    }

    @Override
    public List<Airport> getAllAirports() {
        System.out.println("[LOG] Fetching all airports");
        return decorated.getAllAirports();
    }

    @Override
    public Airport getAirportById(Long id) throws ApiExceptionResponse {
        System.out.println("[LOG] Fetching airport by id: " + id);
        return decorated.getAirportById(id);
    }

    @Override
    public void deleteAirport(Long id) throws ApiExceptionResponse {
        System.out.println("[LOG] Deleting airport with id: " + id);
        decorated.deleteAirport(id);
        System.out.println("[LOG] Airport deleted with id: " + id);
    }

    @Override
    public Airport updateAirport(Airport airport) {
        System.out.println("[LOG] Updating airport with id: " + airport.getId());
        return decorated.updateAirport(airport);
    }
}
