package com.re.cinemabookingapp.repository;

import com.re.cinemabookingapp.entity.Booking;
import com.re.cinemabookingapp.enums.BookingStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingCode(String bookingCode);

    Page<Booking> findByUserIdOrderByBookingDateDesc(Long userId, Pageable pageable);

    long countByUserIdAndStatus(Long userId, BookingStatus status);

    long countByStatus(BookingStatus status);

    @Query("SELECT COALESCE(SUM(b.totalAmount), 0) FROM Booking b WHERE b.status = 'CONFIRMED'")
    BigDecimal sumConfirmedRevenue();

    @Query("SELECT b FROM Booking b ORDER BY b.bookingDate DESC")
    List<Booking> findRecentBookings(Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.bookingCode LIKE %:keyword% " +
           "OR b.user.userProfile.fullName LIKE %:keyword% " +
           "OR b.user.userProfile.phoneNumber LIKE %:keyword%")
    Page<Booking> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
