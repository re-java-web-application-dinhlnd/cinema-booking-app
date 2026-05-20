package com.re.cinemabookingapp.controller.customer;

import com.re.cinemabookingapp.dto.booking.SeatStatusDto;
import com.re.cinemabookingapp.entity.Showtime;
import com.re.cinemabookingapp.enums.ShowtimeStatus;
import com.re.cinemabookingapp.repository.ShowtimeRepository;
import com.re.cinemabookingapp.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final ShowtimeRepository showtimeRepository;
    private final BookingService bookingService;

    @GetMapping("/seats")
    public String seatSelection(@RequestParam Long showtimeId, Model model) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new IllegalArgumentException("Suất chiếu không tồn tại!"));

        if (showtime.getStatus() != ShowtimeStatus.ACTIVE) {
            throw new IllegalArgumentException("Suất chiếu này hiện không khả dụng!");
        }

        if (showtime.getStartTime().before(Timestamp.valueOf(LocalDateTime.now()))) {
            throw new IllegalArgumentException("Suất chiếu này đã bắt đầu!");
        }

        List<SeatStatusDto> seats = bookingService.getSeatStatus(showtimeId);

        model.addAttribute("showtime", showtime);
        model.addAttribute("movie", showtime.getMovie());
        model.addAttribute("seats", seats);
        model.addAttribute("ticketPrice", showtime.getTicketPrice());
        return "customer/seat-selection";
    }
}
