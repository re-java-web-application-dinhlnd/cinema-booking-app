package com.re.cinemabookingapp.controller.customer;

import com.re.cinemabookingapp.dto.booking.BookingCreateDto;
import com.re.cinemabookingapp.dto.booking.ProductItemDto;
import com.re.cinemabookingapp.dto.booking.SeatStatusDto;
import com.re.cinemabookingapp.entity.Booking;
import com.re.cinemabookingapp.entity.Product;
import com.re.cinemabookingapp.entity.Seat;
import com.re.cinemabookingapp.entity.Showtime;
import com.re.cinemabookingapp.entity.User;
import com.re.cinemabookingapp.enums.ProductStatus;
import com.re.cinemabookingapp.enums.SeatType;
import com.re.cinemabookingapp.enums.ShowtimeStatus;
import com.re.cinemabookingapp.repository.ProductRepository;
import com.re.cinemabookingapp.repository.SeatRepository;
import com.re.cinemabookingapp.repository.ShowtimeRepository;
import com.re.cinemabookingapp.service.BookingService;
import lombok.RequiredArgsConstructor;
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

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;
    private final ProductRepository productRepository;
    private final BookingService bookingService;

    private static final BigDecimal VIP_MULTIPLIER = new BigDecimal("1.3");
    private static final BigDecimal SWEETBOX_MULTIPLIER = new BigDecimal("1.5");

    @GetMapping("/seats")
    public String seatSelection(@RequestParam Long showtimeId, Model model) {
        Showtime showtime = validateShowtime(showtimeId);
        List<SeatStatusDto> seats = bookingService.getSeatStatus(showtimeId);

        model.addAttribute("showtime", showtime);
        model.addAttribute("movie", showtime.getMovie());
        model.addAttribute("seats", seats);
        model.addAttribute("ticketPrice", showtime.getTicketPrice());
        return "customer/seat-selection";
    }

    @PostMapping("/confirm")
    public String confirmPage(
            @RequestParam Long showtimeId,
            @RequestParam String seatIds,
            Model model) {

        Showtime showtime = validateShowtime(showtimeId);

        List<Long> seatIdList = parseSeatIds(seatIds);
        if (seatIdList.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn ít nhất 1 ghế!");
        }

        List<Seat> seats = seatRepository.findAllById(seatIdList);
        if (seats.size() != seatIdList.size()) {
            throw new IllegalArgumentException("Một số ghế không hợp lệ!");
        }

        BigDecimal ticketTotal = BigDecimal.ZERO;
        for (Seat seat : seats) {
            if (!seat.getRoom().getId().equals(showtime.getRoom().getId())) {
                throw new IllegalArgumentException("Ghế không thuộc phòng chiếu!");
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
    }

    @PostMapping("/checkout")
    public String checkout(
            @RequestParam Long showtimeId,
            @RequestParam String seatIds,
            @RequestParam(required = false) List<Long> productIds,
            @RequestParam(required = false) List<Integer> quantities,
            @AuthenticationPrincipal User currentUser,
            RedirectAttributes redirectAttributes) {

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
        redirectAttributes.addFlashAttribute("booking", booking);
        return "redirect:/booking/success?id=" + booking.getId();
    }

    @GetMapping("/success")
    public String successPage(@RequestParam Long id, Model model) {
        model.addAttribute("bookingId", id);
        return "customer/booking-success";
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
