package com.example.demo.service.impl;

import com.example.demo.model.Flight;
import com.example.demo.model.User;
import com.example.demo.repository.FlightRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;
    private final PasswordEncoder passwordEncoder;
    private final Random random = new Random();

    public UserServiceImpl(UserRepository userRepository, FlightRepository flightRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.flightRepository = flightRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User addUser(User user) {
        if (user.getId() == null) {
            user.setId(random.nextLong(Long.MAX_VALUE));  // Generare ID doar dacă nu există
        }
        return userRepository.save(user);  // Salvăm utilizatorul în DB
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();  // Returnează toți utilizatorii
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("User with id " + id + " not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new NoSuchElementException("User with email " + email + " not found"));
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);  // Salvează utilizatorul în DB (actualizează dacă există)
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);  // Șterge utilizatorul după ID
    }

    @Override
    public boolean authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }

    @Override
    public void registerUser(User user) {
        String email = user.getEmail();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (!user.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            throw new IllegalArgumentException("Password does not meet requirements");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public User addFlightToFavorites(Long userId, Long flightId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NoSuchElementException("User with id " + userId + " not found"));
        Flight flight = flightRepository.findById(flightId).orElseThrow(() ->
                new NoSuchElementException("Flight with id " + flightId + " not found"));

        if (!user.getFavoriteFlights().contains(flight)) {
            user.getFavoriteFlights().add(flight);  // Adaugă zborul la favorite
            userRepository.save(user);  // Salvează utilizatorul actualizat
        }
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByEmail(username).orElseThrow(() ->
                new NoSuchElementException("User with username " + username + " not found"));
    }

    public void addFavoriteFlight(Long userId, Long flightId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new NoSuchElementException("Flight not found"));

        user.getFavoriteFlights().add(flight);
        userRepository.save(user);
    }

    public void removeFavoriteFlight(Long userId, Long flightId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new NoSuchElementException("Flight not found"));

        user.getFavoriteFlights().remove(flight);
        userRepository.save(user);
    }

    public List<Flight> getFavoriteFlights(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return user.getFavoriteFlights();
    }

    public boolean isFlightFavorite(Long userId, Long flightId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return user.getFavoriteFlights().stream()
                .anyMatch(f -> f.getId().equals(flightId));
    }
}
