package com.example.demo.service.impl;

import com.example.demo.model.Booking;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.FlightRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class BookingServiceImplTest {
    private static final int NUMBER_OF_SEATS = 2;
    private static final double TOTAL_PRICE = 200.0;

    private Booking booking = new Booking(1L, null, null, NUMBER_OF_SEATS, TOTAL_PRICE, null, null);

    // UUT
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private FlightRepository flightRepository;




    @Test
    public void givenNonExistingBooking_whenFindBookingById_thenThrowException() {
        when(bookingRepository.findById(2L)).thenReturn(null);

        assertThrows(NoSuchElementException.class, () -> {
            bookingService.getBookingById(2L);
        });
    }

    @Test
    void givenValidBooking_whenSave_thenReturnSavedBooking() {
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking result = bookingService.addBooking(booking);

        assertNotNull(result);
        assertEquals(NUMBER_OF_SEATS, result.getNumberOfSeats());
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void givenBookings_whenGetAll_thenReturnList() {
        List<Booking> bookings = List.of(booking, new Booking(2L, null, null, 3, 300.0, null, null));
        when(bookingRepository.findAll()).thenReturn(bookings);

        List<Booking> result = bookingService.getAllBookings();

        assertEquals(2, result.size());
        verify(bookingRepository, times(1)).findAll();
    }


}
