package com.re.cinemabookingapp.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecentBookingDto {
    private String bookingCode;
    private String customerName;
    private String movieTitle;
    private Timestamp bookingDate;
    private BigDecimal totalAmount;
    private String status;
}
