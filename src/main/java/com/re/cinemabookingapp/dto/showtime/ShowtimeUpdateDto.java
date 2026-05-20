package com.re.cinemabookingapp.dto.showtime;

import com.re.cinemabookingapp.enums.ShowtimeStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO cho việc cập nhật suất chiếu.
 * Phim và Phòng không cho phép thay đổi — nếu muốn đổi thì tạo suất mới.
 */
@Getter
@Setter
public class ShowtimeUpdateDto {

    @NotNull(message = "Vui lòng chọn ngày giờ bắt đầu")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;

    @NotNull(message = "Vui lòng nhập giá vé")
    @Min(value = 1000, message = "Giá vé tối thiểu là 1.000đ")
    private BigDecimal ticketPrice;

    @NotNull(message = "Vui lòng chọn trạng thái")
    private ShowtimeStatus status;
}
