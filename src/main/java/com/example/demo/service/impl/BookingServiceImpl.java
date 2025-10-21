package com.example.demo.service.impl;

import com.example.demo.model.Booking;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking addBooking(Booking booking) {
        if (booking.getNumberOfSeats() <= 0) {
            throw new IllegalArgumentException("Numărul de locuri trebuie să fie pozitiv!");
        }
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();  // Obține toate rezervările
    }

    @Override
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Booking with id " + id + " not found"));
    }

    @Override
    public List<Booking> getBookingsByUsername(String username) {
        return bookingRepository.findByUserEmail(username);
    }

    @Override
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new NoSuchElementException("Booking with id " + id + " not found");
        }
        bookingRepository.deleteById(id);  // Șterge rezervarea după ID
    }
}
