package com.example.demo.controller;

import com.example.demo.DTO.UserDTO;
import com.example.demo.mapper.FlightMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Flight;
import com.example.demo.model.User;
import com.example.demo.service.FlightService;
import com.example.demo.service.NotificationService;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.OwnerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.demo.DTO.*;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final FlightService flightService;
    private final OwnerServiceImpl ownerService;
    private final NotificationService notificationService;



    public UserController(UserService userService, FlightService flightService, OwnerServiceImpl ownerService, NotificationService notificationService) {
        this.userService = userService;
        this.flightService = flightService;
        this.ownerService = ownerService;
        this.notificationService = notificationService;
    }

    @Operation(
            summary = "Fetch all users",
            description = "Retrieves all user entities"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        List<UserDTO> users = userService.getAllUsers().stream()
                .map(UserMapper::toDto)
                .toList();

        return ResponseEntity.ok(users);
    }


    @Operation(
            summary = "Find user by ID",
            description = "Retrieves a user based on the given ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(UserMapper.toDto(user));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @Operation(
            summary = "Create or update a user",
            description = "Creates a new user or updates an existing one"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created/updated successfully")
    })
    @PostMapping
    public ResponseEntity<?> createOrUpdateUser(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(user));
    }

    @Operation(
            summary = "Delete user by ID",
            description = "Deletes a user based on the given ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully");
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody User user) {
        boolean authenticated = userService.authenticate(user.getEmail(), user.getPassword());

        if (authenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDTO registrationDTO,
                                      BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(result.getFieldErrors().stream()
                            .map(err -> err.getDefaultMessage())
                            .collect(Collectors.joining(", ")));
        }

        try {
            User user = new User();
            user.setEmail(registrationDTO.getEmail());
            user.setPassword(registrationDTO.getPassword()); // Asigură-te că parola este hash-uită în service

            userService.registerUser(user);
            notificationService.notifyAllUsersAirport("User " + user.getEmail() + " registered!");
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(@RequestParam String email) {
        User user = userService.getUserByUsername(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        UserDTO dto = UserMapper.toDto(user);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/user/favorites")
    public List<Flight> getFavoriteFlights(@RequestParam String email) {
        User user = userService.getUserByUsername(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return flightService.getFavoriteFlights(user);
    }

    @PostMapping("/{userId}/favorites/{flightId}")
    public ResponseEntity<String> addFavoriteFlight(
            @PathVariable Long userId,
            @PathVariable Long flightId) {
        try {
            userService.addFavoriteFlight(userId, flightId);
            return ResponseEntity.ok("Flight added to favorites successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/favorites/{flightId}")
    public ResponseEntity<String> removeFavoriteFlight(
            @PathVariable Long userId,
            @PathVariable Long flightId) {
        try {
            userService.removeFavoriteFlight(userId, flightId);
            return ResponseEntity.ok("Flight removed from favorites successfully");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/favorites")
    public ResponseEntity<List<FlightDTO>> getFavoriteFlights(@PathVariable Long userId) {
        try {
            List<Flight> favorites = userService.getFavoriteFlights(userId);
            List<FlightDTO> favoriteDTOs = favorites.stream()
                    .map(FlightMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(favoriteDTOs);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{userId}/favorites/{flightId}/check")
    public ResponseEntity<Boolean> isFlightFavorite(
            @PathVariable Long userId,
            @PathVariable Long flightId) {
        try {
            boolean isFavorite = userService.isFlightFavorite(userId, flightId);
            return ResponseEntity.ok(isFavorite);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
