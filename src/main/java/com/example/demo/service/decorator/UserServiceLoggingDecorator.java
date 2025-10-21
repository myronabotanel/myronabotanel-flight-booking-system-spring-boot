package com.example.demo.service.decorator;

import com.example.demo.model.Flight;
import com.example.demo.model.User;
import com.example.demo.service.UserService;

import java.util.List;

public class UserServiceLoggingDecorator implements UserService {

    private final UserService decorated;

    public UserServiceLoggingDecorator(UserService decorated) {
        this.decorated = decorated;
    }

    @Override
    public User addUser(User user) {
        System.out.println("[LOG] Adding user: " + user);
        User result = decorated.addUser(user);
        System.out.println("[LOG] User added with id: " + result.getId());
        return result;
    }

    @Override
    public List<User> getAllUsers() {
        System.out.println("[LOG] Fetching all users");
        return decorated.getAllUsers();
    }

    @Override
    public User getUserById(Long id) {
        System.out.println("[LOG] Fetching user by id: " + id);
        return decorated.getUserById(id);
    }

    @Override
    public User updateUser(User user) {
        System.out.println("[LOG] Updating user with id: " + user.getId());
        return decorated.updateUser(user);
    }

    @Override
    public void deleteUser(Long id) {
        System.out.println("[LOG] Deleting user with id: " + id);
        decorated.deleteUser(id);
        System.out.println("[LOG] User deleted with id: " + id);
    }

    @Override
    public boolean authenticate(String email, String password) {
        System.out.println("[LOG] Authenticating user with email: " + email);
        return decorated.authenticate(email, password);
    }

    @Override
    public void registerUser(User user) {
        System.out.println("[LOG] Registering user: " + user);
        decorated.registerUser(user);
    }

    @Override
    public User getUserByEmail(String email) {
        System.out.println("[LOG] Fetching user by email: " + email);
        return decorated.getUserByEmail(email);
    }

    @Override
    public User addFlightToFavorites(Long userId, Long flightId) {
        System.out.println("[LOG] Adding flight id " + flightId + " to favorites of user id " + userId);
        return decorated.addFlightToFavorites(userId, flightId);
    }

    @Override
    public User getUserByUsername(String username) {
        System.out.println("[LOG] Fetching user by username: " + username);
        return decorated.getUserByUsername(username);
    }

    @Override
    public void addFavoriteFlight(Long userId, Long flightId) {
        System.out.println("[LOG] Adding favorite flight id " + flightId + " for user id " + userId);
        decorated.addFavoriteFlight(userId, flightId);
    }

    @Override
    public void removeFavoriteFlight(Long userId, Long flightId) {
        System.out.println("[LOG] Removing favorite flight id " + flightId + " for user id " + userId);
        decorated.removeFavoriteFlight(userId, flightId);
    }

    @Override
    public List<Flight> getFavoriteFlights(Long userId) {
        System.out.println("[LOG] Fetching favorite flights for user id: " + userId);
        return decorated.getFavoriteFlights(userId);
    }

    @Override
    public boolean isFlightFavorite(Long userId, Long flightId) {
        System.out.println("[LOG] Checking if flight id " + flightId + " is favorite for user id " + userId);
        return decorated.isFlightFavorite(userId, flightId);
    }
}
