package com.re.cinemabookingapp.mapper;

import com.re.cinemabookingapp.dto.showtime.ShowtimeCreateDto;
import com.re.cinemabookingapp.entity.Showtime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper cho Showtime entity ↔ DTO.
 * Các field cần logic phức tạp (movie, room, endTime) được ignore
 * và xử lý thủ công trong Service layer.
 */
@Mapper(componentModel = "spring")
public interface ShowtimeMapper {

    /**
     * Map ShowtimeCreateDto → Showtime entity.
     * Bỏ qua: movie, room (cần lookup từ DB), endTime (cần tính toán),
     * id, status, createdAt, tickets (auto-generated).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "movie", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    Showtime toEntity(ShowtimeCreateDto dto);
}
