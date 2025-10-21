package com.example.demo.controller;

import com.example.demo.DTO.AirportDTO;
import com.example.demo.exceptions.ApiExceptionResponse;
import com.example.demo.mapper.AirportMapper;
import com.example.demo.model.Airport;
import com.example.demo.service.AirportService;
import com.example.demo.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/airport")
public class AirportController {

    private final AirportService airportService;
    private final NotificationService notificationService;
    public AirportController(AirportService airportService, NotificationService notificationService) {
        this.airportService = airportService;
        this.notificationService = notificationService;
    }

    @Operation(
            summary = "Fetch all airports",
            description = "Fetches all airports entities from the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public ResponseEntity<List<AirportDTO>> findAllAirports() {
        return ResponseEntity.ok(AirportMapper.toDtoList(airportService.getAllAirports()));
    }

    @Operation(
            summary = "Export all airports to XML",
            description = "Exports all airport entities from the database in XML format"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/xml",
                            schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping(value = "/export/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<List<Airport>> exportAirportsXml() {
        List<Airport> airports = airportService.getAllAirports();
        return ResponseEntity.ok(airports);
    }

    @Operation(
            summary = "Find airport by ID",
            description = "Finds an airport based on the provided ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Airport found",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Airport.class))),
            @ApiResponse(responseCode = "404", description = "Airport not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AirportDTO> findAirportById(@PathVariable Long id) throws ApiExceptionResponse {
        try {
            Airport airport = airportService.getAirportById(id);
            return ResponseEntity.ok(AirportMapper.toDto(airport));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @Operation(
            summary = "Create a new airport",
            description = "Creates a new airport entity"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Airport created",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Airport.class))),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PostMapping
    public ResponseEntity<AirportDTO> createAirport(@Valid @RequestBody AirportDTO airportDto) {
        Airport airport = AirportMapper.toEntity(airportDto);
        Airport savedAirport = airportService.addAirport(airport);
        notificationService.notifyAllUsersAirport("A fost adăugat un aeroport nou: " + savedAirport.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(AirportMapper.toDto(savedAirport));
    }


    @Operation(
            summary = "Delete an airport",
            description = "Deletes an airport based on the provided ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Airport not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity deleteAirportById(@PathVariable Long id)throws ApiExceptionResponse {
        airportService.deleteAirport(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Successful operation");
    }

    @Operation(
            summary = "Update airport",
            description = "Updates an existing airport based on the provided ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Airport updated",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Airport.class))),
            @ApiResponse(responseCode = "404", description = "Airport not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AirportDTO> updateAirport(@PathVariable Long id, @Valid @RequestBody AirportDTO airportDto) {
        try {
            Airport airport = AirportMapper.toEntity(airportDto);
            airport.setId(id);
            Airport updatedAirport = airportService.updateAirport(airport);
            return ResponseEntity.ok(AirportMapper.toDto(updatedAirport));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}

