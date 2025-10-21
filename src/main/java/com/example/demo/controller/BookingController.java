package com.example.demo.controller;

import com.example.demo.DTO.BookingDTO;
import com.example.demo.mapper.BookingMapper;
import com.example.demo.model.Booking;
import com.example.demo.model.Flight;
import com.example.demo.model.User;
import com.example.demo.repository.FlightRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.BookingService;
import com.example.demo.service.FlightService;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.NoSuchElementException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Permite accesul din aplicația frontend (React) pe localhost:3000
@RequestMapping("/booking")
@Validated
public class BookingController {

    private final BookingService bookingService;
    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
    private final FlightService flightService;

    private final UserService userService;

    public BookingController(BookingService bookingService, FlightService flightService, UserService userService) {
        this.bookingService = bookingService;
        this.flightService = flightService;
        this.userService = userService;
    }


    @Operation(
            summary = "Fetch all bookings",
            description = "Fetches all booking entities"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @GetMapping
    public ResponseEntity<List<BookingDTO>> findAllBookings() {
        List<BookingDTO> bookingDTOs = bookingService.getAllBookings()
                .stream()
                .map(BookingMapper::toDto)
                .toList();
        return ResponseEntity.ok(bookingDTOs);
    }


    @Operation(
            summary = "Find booking by ID",
            description = "Finds the booking based on the ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking found"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> findBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        if (booking == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(BookingMapper.toDto(booking));
    }


//    @Operation(
//            summary = "Create a new booking",
//            description = "Creates a new booking"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "Booking created successfully")
//    })
//    @PostMapping
//    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
//        Booking createdBooking = bookingService.addBooking(booking);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
//    }

    @Operation(
            summary = "Delete booking by ID",
            description = "Deletes a booking based on the ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookingById(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
            summary = "Update a booking",
            description = "Updates the details of an existing booking"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking updated successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        Booking existingBooking = bookingService.getBookingById(id);
        if (existingBooking == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        booking.setId(id);
        Booking updatedBooking = bookingService.addBooking(booking);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBooking);
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseTicket(
            @NotNull @RequestParam Long flightId,
            @NotBlank @RequestParam String username,
            @Min(1) @RequestParam int numberOfSeats)
    {

        Flight flight = flightService.getFlightById(flightId);
        User user = userService.getUserByUsername(username);

        if (flight == null || user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Zborul sau utilizatorul nu a fost găsit.");
        }

        int availableSeats = flight.getTotalSeats() - (flight.getBookings() != null ? flight.getBookings().size() : 0);
        if (numberOfSeats > availableSeats) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nu există suficiente locuri disponibile.");
        }

        double totalPrice = numberOfSeats * flight.getPricePerSeat();

        Booking booking = Booking.builder()
                .user(user)
                .flight(flight)
                .numberOfSeats(numberOfSeats)
                .totalPrice(totalPrice)
                .departureAirport(flight.getDepartureAirport())
                .arrivalAirport(flight.getArrivalAirport())
                .build();

        bookingService.addBooking(booking);
        //user.getBookings().add(booking);
        //userService.updateUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("Bilet cumpărat cu succes de utilizatorul " + user.getEmail() + "!");
    }
    @Operation(
            summary = "Find bookings by user email",
            description = "Fetches all bookings for a user based on their email"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/bookings")
    public ResponseEntity<List<BookingDTO>> findBookingsByUserEmail(@RequestParam String email) {
        User user = userService.getUserByUsername(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<BookingDTO> bookings = bookingService.getBookingsByUsername(user.getEmail())
                .stream()
                .map(BookingMapper::toDto)
                .toList();

        return ResponseEntity.ok(bookings);
    }



}
