package com.re.cinemabookingapp.repository;

import com.re.cinemabookingapp.entity.Ticket;
import com.re.cinemabookingapp.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT t.seat.id FROM Ticket t WHERE t.showtime.id = :showtimeId " +
           "AND t.booking.status <> 'CANCELLED'")
    List<Long> findBookedSeatIds(@Param("showtimeId") Long showtimeId);

    boolean existsByShowtimeIdAndSeatIdAndBookingStatusNot(
            Long showtimeId, Long seatId, BookingStatus status);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE t.showtime.id = :showtimeId " +
           "AND t.booking.status <> 'CANCELLED'")
    long countBookedSeats(@Param("showtimeId") Long showtimeId);
}
