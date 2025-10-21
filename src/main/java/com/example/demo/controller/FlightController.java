package com.example.demo.controller;

import com.example.demo.DTO.FlightDTO;
import com.example.demo.mapper.FlightMapper;
import com.example.demo.model.Flight;
import com.example.demo.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/flight")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @Operation(summary = "Fetch all flights")
    @GetMapping
    public ResponseEntity<List<FlightDTO>> findAllFlights() {
        List<FlightDTO> flights = flightService.getAllFlights().stream()
                .map(FlightMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(flights);
    }

    @Operation(summary = "Find flight by ID")
    @GetMapping("/{id}")
    public ResponseEntity<FlightDTO> findFlightById(@PathVariable Long id) {
        try {
            Flight flight = flightService.getFlightById(id);
            return ResponseEntity.ok(FlightMapper.toDto(flight));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new flight")
    @PostMapping
    public ResponseEntity<?> createFlight(@Valid @RequestBody FlightDTO flightDto) {
        try {
            Flight flight = FlightMapper.toNewEntity(flightDto);
            Flight createdFlight = flightService.addFlight(flight);
            return ResponseEntity.status(HttpStatus.CREATED).body(FlightMapper.toDto(createdFlight));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Delete flight by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlightById(@PathVariable Long id) {
        try {
            flightService.deleteFlight(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Update an existing flight")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFlight(@PathVariable Long id, @Valid @RequestBody FlightDTO flightDto) {
        try {
            if (!id.equals(flightDto.getId())) {
                return ResponseEntity.badRequest().body("ID in path and body don't match");
            }

            Flight flight = FlightMapper.toNewEntity(flightDto);
            Flight updatedFlight = flightService.updateFlight(flight);
            return ResponseEntity.ok(FlightMapper.toDto(updatedFlight));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Find flights by cities")
    @GetMapping("/search")
    public ResponseEntity<List<FlightDTO>> findFlightsByCities(
            @RequestParam String fromCity,
            @RequestParam String toCity) {
        List<FlightDTO> flights = flightService.findFlightsByCities(fromCity, toCity).stream()
                .map(FlightMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(flights);
    }

    @Operation(summary = "Find flights by maximum price")
    @GetMapping("/search/price")
    public ResponseEntity<List<FlightDTO>> findFlightsByMaxPrice(
            @RequestParam double maxPrice) {
        List<FlightDTO> flights = flightService.findFlightsByMaxPrice(maxPrice).stream()
                .map(FlightMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(flights);
    }
}