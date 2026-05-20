package com.re.cinemabookingapp.service;

import com.re.cinemabookingapp.dto.booking.BookingCreateDto;
import com.re.cinemabookingapp.dto.booking.SeatStatusDto;
import com.re.cinemabookingapp.entity.Booking;
import com.re.cinemabookingapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookingService {

    Booking createBooking(BookingCreateDto dto, User currentUser);

    List<SeatStatusDto> getSeatStatus(Long showtimeId);

    Page<Booking> getUserBookings(Long userId, Pageable pageable);
}
