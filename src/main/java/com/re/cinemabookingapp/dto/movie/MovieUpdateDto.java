package com.re.cinemabookingapp.dto.movie;

import com.re.cinemabookingapp.enums.MovieStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class MovieUpdateDto {

    @NotBlank(message = "Tên phim không được để trống")
    private String title;

    private String description;

    @NotNull(message = "Thời lượng không được để trống")
    @Min(value = 1, message = "Thời lượng phải lớn hơn 0")
    private Integer durationMinutes;

    private Date releaseDate;

    private String trailerUrl;

    @NotNull(message = "Trạng thái không được để trống")
    private MovieStatus status;
}
