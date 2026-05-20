package com.re.cinemabookingapp.dto.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookingCreateDto {

    @NotNull(message = "Vui lòng chọn suất chiếu")
    private Long showtimeId;

    @NotEmpty(message = "Vui lòng chọn ít nhất 1 ghế")
    private List<Long> seatIds;

    @Valid
    private List<ProductItemDto> productItems;
}
