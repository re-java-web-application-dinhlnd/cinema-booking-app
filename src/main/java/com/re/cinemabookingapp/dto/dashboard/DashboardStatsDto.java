package com.re.cinemabookingapp.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    private long totalMovies;
    private long totalTicketsSold;
    private BigDecimal totalRevenue;
    private long totalCustomers;
    private List<RecentBookingDto> recentBookings;
    private List<TopMovieDto> topMovies;
}
