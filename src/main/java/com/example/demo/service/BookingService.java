package com.example.demo.service;

import com.example.demo.model.Booking;

import java.util.List;

public interface BookingService {
    Booking addBooking(Booking booking);
    List<Booking> getAllBookings();
    Booking getBookingById(Long id);
    void deleteBooking(Long id);
    List<Booking> getBookingsByUsername(String username); // obține rezervările pe baza username-ului
}
