package com.example.demo.service.decorator;

import com.example.demo.model.Booking;
import com.example.demo.service.BookingService;

import java.util.List;

public class BookingServiceLoggingDecorator implements BookingService
{
    private final BookingService decorated;
    public BookingServiceLoggingDecorator(BookingService decorated) {
        this.decorated = decorated;
    }
    @Override
    public Booking addBooking(Booking booking) {
        System.out.println("[LOG] Adding booking for user: " + booking.getUser());
        Booking result = decorated.addBooking(booking);
        System.out.println("[LOG] Booking added with id: " + result.getId());
        return result;
    }

    @Override
    public List<Booking> getAllBookings() {
        System.out.println("[LOG] Fetching all bookings");
        return decorated.getAllBookings();
    }

    @Override
    public Booking getBookingById(Long id) {
        System.out.println("[LOG] Fetching booking by id: " + id);
        return decorated.getBookingById(id);
    }

    @Override
    public List<Booking> getBookingsByUsername(String username) {
        System.out.println("[LOG] Fetching bookings for username: " + username);
        return decorated.getBookingsByUsername(username);
    }

    @Override
    public void deleteBooking(Long id) {
        System.out.println("[LOG] Deleting booking with id: " + id);
        decorated.deleteBooking(id);
        System.out.println("[LOG] Booking deleted with id: " + id);
    }

}
