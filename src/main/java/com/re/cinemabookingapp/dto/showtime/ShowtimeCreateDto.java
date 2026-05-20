package com.re.cinemabookingapp.dto.showtime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO cho việc tạo suất chiếu mới.
 */
@Getter
@Setter
public class ShowtimeCreateDto {

    @NotNull(message = "Vui lòng chọn phim")
    private Long movieId;

    @NotNull(message = "Vui lòng chọn phòng chiếu")
    private Long roomId;

    @NotNull(message = "Vui lòng chọn ngày giờ bắt đầu")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;

    @NotNull(message = "Vui lòng nhập giá vé")
    @Min(value = 1000, message = "Giá vé tối thiểu là 1.000đ")
    private BigDecimal ticketPrice;
}
