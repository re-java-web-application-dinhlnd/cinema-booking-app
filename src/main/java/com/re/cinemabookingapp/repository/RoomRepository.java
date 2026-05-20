package com.re.cinemabookingapp.repository;

import com.re.cinemabookingapp.entity.Room;
import com.re.cinemabookingapp.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    /** Lấy danh sách phòng chiếu đang hoạt động (cho dropdown chọn phòng) */
    List<Room> findAllByStatus(RoomStatus status);
}
