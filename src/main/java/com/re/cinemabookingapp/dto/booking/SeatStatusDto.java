package com.re.cinemabookingapp.dto.booking;

import com.re.cinemabookingapp.enums.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SeatStatusDto {
    private Long seatId;
    private String seatName;
    private SeatType seatType;
    private boolean booked;
}
