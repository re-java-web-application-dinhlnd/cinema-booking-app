package com.re.cinemabookingapp.repository;

import com.re.cinemabookingapp.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByRoomIdOrderBySeatNameAsc(Long roomId);

    long countByRoomId(Long roomId);
}
