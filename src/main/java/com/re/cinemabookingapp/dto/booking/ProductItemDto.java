package com.re.cinemabookingapp.dto.booking;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductItemDto {

    @NotNull(message = "Vui lòng chọn sản phẩm")
    private Long productId;

    @NotNull(message = "Vui lòng nhập số lượng")
    @Min(value = 1, message = "Số lượng tối thiểu là 1")
    private Integer quantity;
}
