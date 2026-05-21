package com.re.cinemabookingapp.controller.staff;

import com.re.cinemabookingapp.entity.Booking;
import com.re.cinemabookingapp.entity.Showtime;
import com.re.cinemabookingapp.enums.BookingStatus;
import com.re.cinemabookingapp.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("/pos")
@RequiredArgsConstructor
public class StaffBookingController {

    private static final int PAGE_SIZE = 10;

    private final BookingRepository bookingRepository;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(defaultValue = "") String keyword,
                            @RequestParam(defaultValue = "0") int page,
                            Model model) {
        int safePage = Math.max(0, page);

        Page<Booking> bookings;
        if (keyword != null && !keyword.trim().isEmpty()) {
            bookings = bookingRepository.searchByKeyword(
                    keyword.trim(),
                    PageRequest.of(safePage, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "bookingDate"))
            );
        } else {
            bookings = bookingRepository.findAll(
                    PageRequest.of(safePage, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "bookingDate"))
            );
        }

        model.addAttribute("bookings", bookings);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", safePage);
        return "staff/dashboard";
    }

    @GetMapping("/bookings/{code}")
    public String bookingDetail(@PathVariable String code, Model model,
                                RedirectAttributes redirectAttributes) {
        Booking booking = bookingRepository.findByBookingCode(code).orElse(null);

        if (booking == null) {
            redirectAttributes.addFlashAttribute("toastMessage", "Đơn đặt vé không tồn tại!");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/pos/dashboard";
        }

        boolean canCheckIn = isEligibleForCheckIn(booking);
        model.addAttribute("booking", booking);
        model.addAttribute("canCheckIn", canCheckIn);
        return "staff/booking-detail";
    }

    @PostMapping("/bookings/{code}/checkin")
    @Transactional
    public String checkIn(@PathVariable String code,
                          RedirectAttributes redirectAttributes) {
        Booking booking = bookingRepository.findByBookingCode(code).orElse(null);

        if (booking == null) {
            redirectAttributes.addFlashAttribute("toastMessage", "Đơn đặt vé không tồn tại!");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/pos/dashboard";
        }

        if (booking.getStatus() == BookingStatus.CHECKED_IN) {
            redirectAttributes.addFlashAttribute("toastMessage", "Đơn này đã được check-in trước đó!");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/pos/bookings/" + code;
        }

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            redirectAttributes.addFlashAttribute("toastMessage", "Chỉ có thể check-in đơn đã xác nhận!");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/pos/bookings/" + code;
        }

        if (booking.getTickets().isEmpty()) {
            redirectAttributes.addFlashAttribute("toastMessage", "Đơn không có vé hợp lệ!");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/pos/bookings/" + code;
        }

        Showtime showtime = booking.getTickets().get(0).getShowtime();
        LocalDate showtimeDate = showtime.getStartTime().toLocalDateTime().toLocalDate();
        LocalDate today = LocalDate.now();

        if (!showtimeDate.equals(today)) {
            redirectAttributes.addFlashAttribute("toastMessage",
                    "Chỉ có thể check-in cho suất chiếu trong ngày hôm nay!");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/pos/bookings/" + code;
        }

        if (showtime.getStartTime().before(Timestamp.valueOf(LocalDateTime.now().minusMinutes(30)))) {
            redirectAttributes.addFlashAttribute("toastMessage",
                    "Suất chiếu đã bắt đầu quá 30 phút, không thể check-in!");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/pos/bookings/" + code;
        }

        booking.setStatus(BookingStatus.CHECKED_IN);
        bookingRepository.save(booking);

        log.info("Check-in successful — booking: {}", code);

        redirectAttributes.addFlashAttribute("toastMessage", "Check-in thành công!");
        redirectAttributes.addFlashAttribute("toastType", "success");
        return "redirect:/pos/bookings/" + code;
    }

    private boolean isEligibleForCheckIn(Booking booking) {
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            return false;
        }
        if (booking.getTickets() == null || booking.getTickets().isEmpty()) {
            return false;
        }

        Showtime showtime = booking.getTickets().get(0).getShowtime();
        LocalDate showtimeDate = showtime.getStartTime().toLocalDateTime().toLocalDate();

        if (!showtimeDate.equals(LocalDate.now())) {
            return false;
        }

        return !showtime.getStartTime().before(Timestamp.valueOf(LocalDateTime.now().minusMinutes(30)));
    }
}
