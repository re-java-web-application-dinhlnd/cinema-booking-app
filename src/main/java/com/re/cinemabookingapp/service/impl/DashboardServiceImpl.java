package com.re.cinemabookingapp.service.impl;

import com.re.cinemabookingapp.dto.dashboard.DashboardStatsDto;
import com.re.cinemabookingapp.dto.dashboard.RecentBookingDto;
import com.re.cinemabookingapp.dto.dashboard.TopMovieDto;
import com.re.cinemabookingapp.entity.Booking;
import com.re.cinemabookingapp.enums.MovieStatus;
import com.re.cinemabookingapp.enums.UserRole;
import com.re.cinemabookingapp.repository.BookingRepository;
import com.re.cinemabookingapp.repository.MovieRepository;
import com.re.cinemabookingapp.repository.TicketRepository;
import com.re.cinemabookingapp.repository.UserRepository;
import com.re.cinemabookingapp.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private static final int RECENT_BOOKINGS_LIMIT = 8;
    private static final int TOP_MOVIES_LIMIT = 5;

    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @Override
    public DashboardStatsDto getDashboardStats() {
        long totalMovies = movieRepository.countByStatus(MovieStatus.ACTIVE);
        long totalTicketsSold = ticketRepository.countConfirmedTickets();
        BigDecimal totalRevenue = bookingRepository.sumConfirmedRevenue();
        long totalCustomers = userRepository.countByRole(UserRole.CUSTOMER);

        List<RecentBookingDto> recentBookings = buildRecentBookings();
        List<TopMovieDto> topMovies = buildTopMovies();

        return new DashboardStatsDto(
                totalMovies,
                totalTicketsSold,
                totalRevenue,
                totalCustomers,
                recentBookings,
                topMovies
        );
    }

    private List<RecentBookingDto> buildRecentBookings() {
        return bookingRepository.findRecentBookings(PageRequest.of(0, RECENT_BOOKINGS_LIMIT))
                .stream()
                .map(this::toRecentBookingDto)
                .collect(Collectors.toList());
    }

    private RecentBookingDto toRecentBookingDto(Booking booking) {
        String customerName = "N/A";
        if (booking.getUser() != null && booking.getUser().getUserProfile() != null) {
            customerName = booking.getUser().getUserProfile().getFullName();
        }

        String movieTitle = "N/A";
        if (booking.getTickets() != null && !booking.getTickets().isEmpty()) {
            movieTitle = booking.getTickets().get(0).getShowtime().getMovie().getTitle();
        }

        return new RecentBookingDto(
                booking.getBookingCode(),
                customerName,
                movieTitle,
                booking.getBookingDate(),
                booking.getTotalAmount(),
                booking.getStatus().name()
        );
    }

    private List<TopMovieDto> buildTopMovies() {
        return ticketRepository.findTopMovieStats(PageRequest.of(0, TOP_MOVIES_LIMIT))
                .stream()
                .map(row -> new TopMovieDto(
                        (String) row[0],
                        (String) row[1],
                        (Long) row[2],
                        (BigDecimal) row[3]
                ))
                .collect(Collectors.toList());
    }
}
