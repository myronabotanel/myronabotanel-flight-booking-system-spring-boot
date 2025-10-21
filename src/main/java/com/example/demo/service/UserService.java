package com.example.demo.service;

import com.example.demo.model.Flight;
import com.example.demo.model.User;

import java.util.List;

public interface UserService {
    User addUser(User user);
    List<User> getAllUsers();
    User getUserById(Long id);
    User updateUser(User user);
    void deleteUser(Long id);
    boolean authenticate(String email, String password);
    void registerUser(User user);
    User getUserByEmail(String email);
    User addFlightToFavorites(Long userId, Long flightId);
    User getUserByUsername(String username);
    
    // New methods for favorite flights
    void addFavoriteFlight(Long userId, Long flightId);
    void removeFavoriteFlight(Long userId, Long flightId);
    List<Flight> getFavoriteFlights(Long userId);
    boolean isFlightFavorite(Long userId, Long flightId);

}
