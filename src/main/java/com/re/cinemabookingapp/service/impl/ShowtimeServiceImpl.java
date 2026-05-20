package com.re.cinemabookingapp.service.impl;

import com.re.cinemabookingapp.dto.showtime.ShowtimeCreateDto;
import com.re.cinemabookingapp.dto.showtime.ShowtimeUpdateDto;
import com.re.cinemabookingapp.entity.Movie;
import com.re.cinemabookingapp.entity.Room;
import com.re.cinemabookingapp.entity.Showtime;
import com.re.cinemabookingapp.enums.ShowtimeStatus;
import com.re.cinemabookingapp.mapper.ShowtimeMapper;
import com.re.cinemabookingapp.repository.MovieRepository;
import com.re.cinemabookingapp.repository.RoomRepository;
import com.re.cinemabookingapp.repository.ShowtimeRepository;
import com.re.cinemabookingapp.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ShowtimeMapper showtimeMapper;

    /** Buffer dọn phòng sau mỗi suất chiếu (phút) */
    private static final int CLEANUP_BUFFER_MINUTES = 15;

    @Override
    @Transactional
    public Showtime create(ShowtimeCreateDto dto) {
        // 1. Lookup Movie & Room
        Movie movie = movieRepository.findById(dto.getMovieId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phim với ID: " + dto.getMovieId()));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng chiếu với ID: " + dto.getRoomId()));

        // 2. Tính startTime và endTime
        Timestamp startTime = Timestamp.valueOf(dto.getStartTime());
        Timestamp endTime = calculateEndTime(dto.getStartTime(), movie.getDurationMinutes());

        // 3. Kiểm tra xung đột phòng
        validateNoConflict(room.getId(), null, startTime, endTime);

        // 4. Map DTO → Entity bằng MapStruct + set các field cần logic
        Showtime showtime = showtimeMapper.toEntity(dto);
        showtime.setMovie(movie);
        showtime.setRoom(room);
        showtime.setStartTime(startTime);
        showtime.setEndTime(endTime);
        showtime.setTicketPrice(dto.getTicketPrice());
        showtime.setStatus(ShowtimeStatus.ACTIVE);

        log.info("Tạo suất chiếu: '{}' tại '{}', {} - {}",
                movie.getTitle(), room.getName(), startTime, endTime);

        return showtimeRepository.save(showtime);
    }

    @Override
    @Transactional
    public Showtime update(Long id, ShowtimeUpdateDto dto) {
        Showtime showtime = getById(id);

        // 1. Tính lại endTime mới
        Timestamp newStartTime = Timestamp.valueOf(dto.getStartTime());
        Timestamp newEndTime = calculateEndTime(dto.getStartTime(), showtime.getMovie().getDurationMinutes());

        // 2. Kiểm tra xung đột (loại trừ chính nó)
        validateNoConflict(showtime.getRoom().getId(), id, newStartTime, newEndTime);

        // 3. Cập nhật các field
        showtime.setStartTime(newStartTime);
        showtime.setEndTime(newEndTime);
        showtime.setTicketPrice(dto.getTicketPrice());
        showtime.setStatus(dto.getStatus());

        log.info("Cập nhật suất chiếu #{}: '{}' tại '{}'",
                id, showtime.getMovie().getTitle(), showtime.getRoom().getName());

        return showtimeRepository.save(showtime);
    }

    @Override
    public Page<Showtime> search(Long movieId, Long roomId, ShowtimeStatus status, Pageable pageable) {
        return showtimeRepository.searchShowtimes(movieId, roomId, status, pageable);
    }

    @Override
    public Showtime getById(Long id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy suất chiếu với ID: " + id));
    }

    @Override
    @Transactional
    public void softHide(Long id) {
        Showtime showtime = getById(id);
        showtime.setStatus(ShowtimeStatus.HIDDEN);
        showtimeRepository.save(showtime);
        log.info("Ẩn suất chiếu #{}: '{}' tại '{}'",
                id, showtime.getMovie().getTitle(), showtime.getRoom().getName());
    }

    /**
     * Tính endTime = startTime + durationMinutes + buffer dọn phòng.
     */
    private Timestamp calculateEndTime(LocalDateTime startTime, int durationMinutes) {
        LocalDateTime endTime = startTime.plusMinutes(durationMinutes + CLEANUP_BUFFER_MINUTES);
        return Timestamp.valueOf(endTime);
    }

    /**
     * Kiểm tra xung đột phòng chiếu.
     * @param excludeId ID suất chiếu cần loại trừ (null nếu đang tạo mới)
     */
    private void validateNoConflict(Long roomId, Long excludeId, Timestamp startTime, Timestamp endTime) {
        List<Showtime> conflicts;

        if (excludeId != null) {
            conflicts = showtimeRepository.findConflictingExcluding(roomId, excludeId, startTime, endTime);
        } else {
            conflicts = showtimeRepository.findConflicting(roomId, startTime, endTime);
        }

        if (!conflicts.isEmpty()) {
            Showtime conflict = conflicts.get(0);
            throw new IllegalArgumentException(
                    String.format("Phòng chiếu đã có suất chiếu '%s' từ %s đến %s trong khung giờ này!",
                            conflict.getMovie().getTitle(),
                            conflict.getStartTime(),
                            conflict.getEndTime()));
        }
    }
}
