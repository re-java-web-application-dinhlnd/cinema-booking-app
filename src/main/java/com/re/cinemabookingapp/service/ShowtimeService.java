package com.re.cinemabookingapp.service;

import com.re.cinemabookingapp.dto.showtime.ShowtimeCreateDto;
import com.re.cinemabookingapp.dto.showtime.ShowtimeUpdateDto;
import com.re.cinemabookingapp.entity.Showtime;
import com.re.cinemabookingapp.enums.ShowtimeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service quản lý suất chiếu.
 */
public interface ShowtimeService {

    /** Tạo suất chiếu mới — tự tính endTime + kiểm tra xung đột phòng */
    Showtime create(ShowtimeCreateDto dto);

    /** Cập nhật suất chiếu — tái kiểm tra xung đột */
    Showtime update(Long id, ShowtimeUpdateDto dto);

    /** Danh sách suất chiếu có phân trang + filter */
    Page<Showtime> search(Long movieId, Long roomId, ShowtimeStatus status, Pageable pageable);

    /** Lấy chi tiết 1 suất chiếu */
    Showtime getById(Long id);

    /** Ẩn suất chiếu (ACTIVE → HIDDEN) */
    void softHide(Long id);
}
