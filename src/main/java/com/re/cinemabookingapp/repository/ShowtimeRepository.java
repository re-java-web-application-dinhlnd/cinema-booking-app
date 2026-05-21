package com.re.cinemabookingapp.repository;

import com.re.cinemabookingapp.entity.Showtime;
import com.re.cinemabookingapp.enums.ShowtimeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    /**
     * Tìm các suất chiếu bị xung đột trong cùng phòng.
     * Logic overlap: 2 khoảng thời gian chồng lấn khi (start1 < end2) AND (start2 < end1)
     * Cộng thêm 15 phút buffer dọn phòng vào endTime của suất chiếu đã tồn tại.
     */
    @Query("SELECT s FROM Showtime s WHERE s.room.id = :roomId " +
            "AND s.status = 'ACTIVE' " +
            "AND s.startTime < :newEnd " +
            "AND FUNCTION('TIMESTAMPADD', MINUTE, 15, s.endTime) > :newStart")
    List<Showtime> findConflicting(@Param("roomId") Long roomId,
                                   @Param("newStart") Timestamp newStart,
                                   @Param("newEnd") Timestamp newEnd);

    /**
     * Tìm xung đột nhưng loại trừ chính suất chiếu đang sửa (dùng khi UPDATE).
     * Cộng thêm 15 phút buffer dọn phòng.
     */
    @Query("SELECT s FROM Showtime s WHERE s.room.id = :roomId " +
            "AND s.id <> :excludeId " +
            "AND s.status = 'ACTIVE' " +
            "AND s.startTime < :newEnd " +
            "AND FUNCTION('TIMESTAMPADD', MINUTE, 15, s.endTime) > :newStart")
    List<Showtime> findConflictingExcluding(@Param("roomId") Long roomId,
                                            @Param("excludeId") Long excludeId,
                                            @Param("newStart") Timestamp newStart,
                                            @Param("newEnd") Timestamp newEnd);

    /**
     * Danh sách suất chiếu có phân trang + filter theo phim, phòng, trạng thái.
     */
    @Query("SELECT s FROM Showtime s " +
            "WHERE (:movieId IS NULL OR s.movie.id = :movieId) " +
            "AND (:roomId IS NULL OR s.room.id = :roomId) " +
            "AND (:status IS NULL OR s.status = :status)")
    Page<Showtime> searchShowtimes(@Param("movieId") Long movieId,
                                   @Param("roomId") Long roomId,
                                   @Param("status") ShowtimeStatus status,
                                   Pageable pageable);

    @Query("SELECT s FROM Showtime s WHERE s.movie.id = :movieId " +
           "AND s.status = 'ACTIVE' " +
           "AND s.startTime >= :dayStart AND s.startTime < :dayEnd " +
           "ORDER BY s.room.name, s.startTime")
    List<Showtime> findActiveByMovieAndDate(@Param("movieId") Long movieId,
                                            @Param("dayStart") Timestamp dayStart,
                                            @Param("dayEnd") Timestamp dayEnd);

    boolean existsByMovieIdAndStatusAndStartTimeAfter(Long movieId, ShowtimeStatus status, Timestamp time);
}
