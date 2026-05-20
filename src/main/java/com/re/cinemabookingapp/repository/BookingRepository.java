package com.re.cinemabookingapp.repository;

import com.re.cinemabookingapp.entity.Booking;
import com.re.cinemabookingapp.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByUserIdOrderByBookingDateDesc(Long userId, Pageable pageable);

    long countByUserIdAndStatus(Long userId, BookingStatus status);
}
