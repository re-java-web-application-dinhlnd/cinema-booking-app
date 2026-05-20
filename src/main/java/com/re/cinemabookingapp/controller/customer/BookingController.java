package com.re.cinemabookingapp.controller.customer;

import com.re.cinemabookingapp.dto.booking.BookingCreateDto;
import com.re.cinemabookingapp.dto.booking.ProductItemDto;
import com.re.cinemabookingapp.dto.booking.SeatStatusDto;
import com.re.cinemabookingapp.entity.Booking;
import com.re.cinemabookingapp.entity.Product;
import com.re.cinemabookingapp.entity.Seat;
import com.re.cinemabookingapp.entity.Showtime;
import com.re.cinemabookingapp.entity.User;
import com.re.cinemabookingapp.configuration.security.CustomUserDetails;
import com.re.cinemabookingapp.enums.ProductStatus;
import com.re.cinemabookingapp.enums.SeatType;
import com.re.cinemabookingapp.enums.ShowtimeStatus;
import com.re.cinemabookingapp.repository.BookingRepository;
import com.re.cinemabookingapp.repository.ProductRepository;
import com.re.cinemabookingapp.repository.SeatRepository;
import com.re.cinemabookingapp.repository.ShowtimeRepository;
import com.re.cinemabookingapp.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final ProductRepository productRepository;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    private static final BigDecimal VIP_MULTIPLIER = new BigDecimal("1.3");
    private static final BigDecimal SWEETBOX_MULTIPLIER = new BigDecimal("1.5");
    private static final int MAX_SEATS = 8;
    private static final int HISTORY_PAGE_SIZE = 10;

    @GetMapping("/seats")
    public String seatSelection(@RequestParam Long showtimeId, Model model,
                                RedirectAttributes redirectAttributes) {
        try {
            Showtime showtime = validateShowtime(showtimeId);
            List<SeatStatusDto> seats = bookingService.getSeatStatus(showtimeId);

            model.addAttribute("showtime", showtime);
            model.addAttribute("movie", showtime.getMovie());
            model.addAttribute("seats", seats);
            model.addAttribute("ticketPrice", showtime.getTicketPrice());
            return "customer/seat-selection";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("toastMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/movies";
        }
    }

    @PostMapping("/confirm")
    public String confirmPage(
            @RequestParam Long showtimeId,
            @RequestParam String seatIds,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            Showtime showtime = validateShowtime(showtimeId);
            List<Long> seatIdList = parseSeatIds(seatIds);

            if (seatIdList.isEmpty()) {
                throw new IllegalArgumentException("Vui lòng chọn ít nhất 1 ghế!");
            }
            if (seatIdList.size() > MAX_SEATS) {
                throw new IllegalArgumentException("Tối đa " + MAX_SEATS + " ghế mỗi lần đặt!");
            }

            List<Seat> seats = seatRepository.findAllById(seatIdList);
            if (seats.size() != seatIdList.size()) {
                throw new IllegalArgumentException("Một số ghế không hợp lệ!");
            }

            BigDecimal ticketTotal = BigDecimal.ZERO;
            for (Seat seat : seats) {
                if (!seat.getRoom().getId().equals(showtime.getRoom().getId())) {
                    throw new IllegalArgumentException("Ghế " + seat.getSeatName() + " không thuộc phòng chiếu này!");
                }
                ticketTotal = ticketTotal.add(calculateSeatPrice(showtime.getTicketPrice(), seat.getSeatType()));
            }

            List<Product> products = productRepository.findByStatusOrderByTypeAscNameAsc(ProductStatus.ACTIVE);

            String seatNames = seats.stream()
                    .map(Seat::getSeatName)
                    .sorted()
                    .collect(Collectors.joining(", "));

            model.addAttribute("showtime", showtime);
            model.addAttribute("movie", showtime.getMovie());
            model.addAttribute("selectedSeats", seats);
            model.addAttribute("seatNames", seatNames);
            model.addAttribute("seatIdsCsv", seatIds);
            model.addAttribute("ticketTotal", ticketTotal);
            model.addAttribute("products", products);
            return "customer/booking-confirm";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("toastMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/booking/seats?showtimeId=" + showtimeId;
        }
    }

    @PostMapping("/checkout")
    public String checkout(
            @RequestParam Long showtimeId,
            @RequestParam String seatIds,
            @RequestParam(required = false) List<Long> productIds,
            @RequestParam(required = false) List<Integer> quantities,
            @AuthenticationPrincipal CustomUserDetails principal,
            RedirectAttributes redirectAttributes) {

        try {
            if (principal == null) {
                throw new IllegalArgumentException("Vui lòng đăng nhập để đặt vé!");
            }
            User currentUser = principal.getUser();

            BookingCreateDto dto = new BookingCreateDto();
            dto.setShowtimeId(showtimeId);
            dto.setSeatIds(parseSeatIds(seatIds));

            if (productIds != null && quantities != null) {
                List<ProductItemDto> items = new ArrayList<>();
                for (int i = 0; i < productIds.size(); i++) {
                    int qty = (i < quantities.size()) ? quantities.get(i) : 0;
                    if (qty > 0) {
                        ProductItemDto item = new ProductItemDto();
                        item.setProductId(productIds.get(i));
                        item.setQuantity(qty);
                        items.add(item);
                    }
                }
                dto.setProductItems(items);
            }

            Booking booking = bookingService.createBooking(dto, currentUser);
            return "redirect:/booking/success?code=" + booking.getBookingCode();

        } catch (IllegalArgumentException e) {
            log.warn("Booking failed — {}", e.getMessage());
            redirectAttributes.addFlashAttribute("toastMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/booking/seats?showtimeId=" + showtimeId;
        } catch (DataIntegrityViolationException e) {
            log.error("Booking race condition — showtimeId: {}, seatIds: {}", showtimeId, seatIds, e);
            redirectAttributes.addFlashAttribute("toastMessage",
                    "Ghế bạn chọn vừa được người khác đặt. Vui lòng chọn lại!");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/booking/seats?showtimeId=" + showtimeId;
        }
    }

    @GetMapping("/success")
    public String successPage(@RequestParam String code,
                              @AuthenticationPrincipal CustomUserDetails principal,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        Booking booking = bookingRepository.findByBookingCode(code).orElse(null);

        if (booking == null) {
            redirectAttributes.addFlashAttribute("toastMessage", "Đơn đặt vé không tồn tại!");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/movies";
        }

        if (!booking.getUser().getId().equals(principal.getUser().getId())) {
            redirectAttributes.addFlashAttribute("toastMessage", "Bạn không có quyền xem đơn này!");
            redirectAttributes.addFlashAttribute("toastType", "error");
            return "redirect:/movies";
        }

        Showtime showtime = booking.getTickets().isEmpty()
                ? null
                : booking.getTickets().get(0).getShowtime();

        String seatNames = booking.getTickets().stream()
                .map(t -> t.getSeat().getSeatName())
                .sorted()
                .collect(Collectors.joining(", "));

        BigDecimal ticketTotal = booking.getTickets().stream()
                .map(t -> t.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal comboTotal = booking.getBookingProducts().stream()
                .map(bp -> bp.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("booking", booking);
        model.addAttribute("showtime", showtime);
        model.addAttribute("movie", showtime != null ? showtime.getMovie() : null);
        model.addAttribute("seatNames", seatNames);
        model.addAttribute("ticketTotal", ticketTotal);
        model.addAttribute("comboTotal", comboTotal);

        boolean canCancel = false;
        if (booking.getStatus().name().equals("CONFIRMED") && showtime != null) {
            LocalDateTime deadline = showtime.getStartTime().toLocalDateTime().minusHours(24);
            canCancel = LocalDateTime.now().isBefore(deadline);
        }
        model.addAttribute("canCancel", canCancel);

        return "customer/booking-success";
    }

    @GetMapping("/history")
    public String bookingHistory(@RequestParam(defaultValue = "0") int page,
                                 @AuthenticationPrincipal CustomUserDetails principal,
                                 Model model) {
        Page<Booking> bookings = bookingService.getUserBookings(
                principal.getUser().getId(), PageRequest.of(page, HISTORY_PAGE_SIZE));

        java.util.Set<String> cancelableCodes = new java.util.HashSet<>();
        for (Booking b : bookings.getContent()) {
            if (b.getStatus() == com.re.cinemabookingapp.enums.BookingStatus.CONFIRMED
                    && !b.getTickets().isEmpty()) {
                Showtime st = b.getTickets().get(0).getShowtime();
                LocalDateTime deadline = st.getStartTime().toLocalDateTime().minusHours(24);
                if (LocalDateTime.now().isBefore(deadline)) {
                    cancelableCodes.add(b.getBookingCode());
                }
            }
        }

        model.addAttribute("bookings", bookings);
        model.addAttribute("currentPage", page);
        model.addAttribute("cancelableCodes", cancelableCodes);
        return "customer/booking-history";
    }

    @PostMapping("/cancel")
    public String cancelBooking(@RequestParam String code,
                                @AuthenticationPrincipal CustomUserDetails principal,
                                RedirectAttributes redirectAttributes) {
        try {
            if (principal == null) {
                throw new IllegalArgumentException("Vui lòng đăng nhập!");
            }

            bookingService.cancelBooking(code, principal.getUser());
            redirectAttributes.addFlashAttribute("toastMessage", "Hủy vé thành công!");
            redirectAttributes.addFlashAttribute("toastType", "success");

        } catch (IllegalArgumentException e) {
            log.warn("Cancel failed — code: {}, reason: {}", code, e.getMessage());
            redirectAttributes.addFlashAttribute("toastMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("toastType", "error");
        }

        return "redirect:/booking/history";
    }

    private Showtime validateShowtime(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new IllegalArgumentException("Suất chiếu không tồn tại!"));

        if (showtime.getStatus() != ShowtimeStatus.ACTIVE) {
            throw new IllegalArgumentException("Suất chiếu này hiện không khả dụng!");
        }
        if (showtime.getStartTime().before(Timestamp.valueOf(LocalDateTime.now()))) {
            throw new IllegalArgumentException("Suất chiếu này đã bắt đầu!");
        }
        return showtime;
    }

    private List<Long> parseSeatIds(String seatIds) {
        if (seatIds == null || seatIds.isBlank()) {
            return List.of();
        }
        return Arrays.stream(seatIds.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

    private BigDecimal calculateSeatPrice(BigDecimal basePrice, SeatType seatType) {
        return switch (seatType) {
            case VIP -> basePrice.multiply(VIP_MULTIPLIER);
            case SWEETBOX -> basePrice.multiply(SWEETBOX_MULTIPLIER);
            default -> basePrice;
        };
    }
}
