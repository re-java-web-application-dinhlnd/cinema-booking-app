package com.re.cinemabookingapp.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopMovieDto {
    private String title;
    private String posterUrl;
    private long ticketCount;
    private BigDecimal revenue;
}
