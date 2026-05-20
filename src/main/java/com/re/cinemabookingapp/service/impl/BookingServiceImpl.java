package com.re.cinemabookingapp.service.impl;

import com.re.cinemabookingapp.dto.booking.BookingCreateDto;
import com.re.cinemabookingapp.dto.booking.ProductItemDto;
import com.re.cinemabookingapp.dto.booking.SeatStatusDto;
import com.re.cinemabookingapp.entity.*;
import com.re.cinemabookingapp.enums.BookingStatus;
import com.re.cinemabookingapp.enums.ProductStatus;
import com.re.cinemabookingapp.enums.SeatType;
import com.re.cinemabookingapp.enums.ShowtimeStatus;
import com.re.cinemabookingapp.repository.*;
import com.re.cinemabookingapp.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;
    private final ProductRepository productRepository;
    private final ShowtimeRepository showtimeRepository;

    private static final BigDecimal VIP_MULTIPLIER = new BigDecimal("1.3");
    private static final BigDecimal SWEETBOX_MULTIPLIER = new BigDecimal("1.5");
    private static final int MAX_SEATS_PER_BOOKING = 8;
    private static final int MAX_PRODUCT_QUANTITY = 10;
    private static final int CANCEL_DEADLINE_HOURS = 24;

    @Override
    @Transactional
    public Booking createBooking(BookingCreateDto dto, User currentUser) {
        Showtime showtime = showtimeRepository.findById(dto.getShowtimeId())
                .orElseThrow(() -> new IllegalArgumentException("Suất chiếu không tồn tại!"));

        if (showtime.getStatus() != ShowtimeStatus.ACTIVE) {
            throw new IllegalArgumentException("Suất chiếu này hiện không khả dụng!");
        }

        if (showtime.getStartTime().before(Timestamp.valueOf(LocalDateTime.now()))) {
            throw new IllegalArgumentException("Suất chiếu này đã bắt đầu, không thể đặt vé!");
        }

        List<Long> seatIds = dto.getSeatIds();
        if (seatIds == null || seatIds.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn ít nhất 1 ghế!");
        }
        if (seatIds.size() > MAX_SEATS_PER_BOOKING) {
            throw new IllegalArgumentException("Tối đa " + MAX_SEATS_PER_BOOKING + " ghế mỗi lần đặt!");
        }

        Set<Long> uniqueSeatIds = new HashSet<>(seatIds);
        if (uniqueSeatIds.size() != seatIds.size()) {
            throw new IllegalArgumentException("Không được chọn trùng ghế!");
        }

        List<Seat> seats = seatRepository.findAllById(seatIds);
        if (seats.size() != seatIds.size()) {
            throw new IllegalArgumentException("Một số ghế không tồn tại!");
        }

        Long roomId = showtime.getRoom().getId();
        for (Seat seat : seats) {
            if (!seat.getRoom().getId().equals(roomId)) {
                throw new IllegalArgumentException("Ghế " + seat.getSeatName() + " không thuộc phòng chiếu này!");
            }
        }

        List<Long> bookedSeatIds = ticketRepository.findBookedSeatIds(showtime.getId());
        List<String> conflictSeats = seats.stream()
                .filter(s -> bookedSeatIds.contains(s.getId()))
                .map(Seat::getSeatName)
                .collect(Collectors.toList());
        if (!conflictSeats.isEmpty()) {
            throw new IllegalArgumentException("Ghế " + String.join(", ", conflictSeats) + " đã được đặt bởi người khác!");
        }

        BigDecimal ticketPrice = showtime.getTicketPrice();
        BigDecimal ticketTotal = BigDecimal.ZERO;
        List<Ticket> tickets = new ArrayList<>();

        for (Seat seat : seats) {
            BigDecimal price = calculateSeatPrice(ticketPrice, seat.getSeatType());
            Ticket ticket = new Ticket();
            ticket.setShowtime(showtime);
            ticket.setSeat(seat);
            ticket.setPrice(price);
            tickets.add(ticket);
            ticketTotal = ticketTotal.add(price);
        }

        BigDecimal productTotal = BigDecimal.ZERO;
        List<BookingProduct> bookingProducts = new ArrayList<>();

        if (dto.getProductItems() != null) {
            for (ProductItemDto item : dto.getProductItems()) {
                if (item.getQuantity() == null || item.getQuantity() <= 0) continue;
                if (item.getQuantity() > MAX_PRODUCT_QUANTITY) {
                    throw new IllegalArgumentException("Số lượng sản phẩm tối đa là " + MAX_PRODUCT_QUANTITY + "!");
                }

                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại!"));

                if (product.getStatus() != ProductStatus.ACTIVE) {
                    throw new IllegalArgumentException("Sản phẩm '" + product.getName() + "' hiện đã ngừng bán!");
                }

                BookingProduct bp = new BookingProduct();
                bp.setProduct(product);
                bp.setQuantity(item.getQuantity());
                bp.setPrice(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                bookingProducts.add(bp);
                productTotal = productTotal.add(bp.getPrice());
            }
        }

        Booking booking = new Booking();
        booking.setUser(currentUser);
        booking.setTotalAmount(ticketTotal.add(productTotal));
        booking.setStatus(BookingStatus.CONFIRMED);

        tickets.forEach(t -> t.setBooking(booking));
        booking.setTickets(tickets);

        bookingProducts.forEach(bp -> bp.setBooking(booking));
        booking.setBookingProducts(bookingProducts);

        Booking saved = bookingRepository.save(booking);

        log.info("Booking #{} created — User: {}, Showtime: {}, Seats: {}, Total: {}",
                saved.getId(), currentUser.getUsername(),
                showtime.getId(),
                seats.stream().map(Seat::getSeatName).collect(Collectors.joining(", ")),
                saved.getTotalAmount());

        return saved;
    }

    @Override
    public List<SeatStatusDto> getSeatStatus(Long showtimeId) {
        Showtime showtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new IllegalArgumentException("Suất chiếu không tồn tại!"));

        List<Seat> seats = seatRepository.findByRoomIdOrderBySeatNameAsc(showtime.getRoom().getId());
        List<Long> bookedIds = ticketRepository.findBookedSeatIds(showtimeId);
        Set<Long> bookedSet = new HashSet<>(bookedIds);

        return seats.stream()
                .map(s -> new SeatStatusDto(s.getId(), s.getSeatName(), s.getSeatType(), bookedSet.contains(s.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public Page<Booking> getUserBookings(Long userId, Pageable pageable) {
        return bookingRepository.findByUserIdOrderByBookingDateDesc(userId, pageable);
    }

    @Override
    @Transactional
    public void cancelBooking(String bookingCode, User currentUser) {
        Booking booking = bookingRepository.findByBookingCode(bookingCode)
                .orElseThrow(() -> new IllegalArgumentException("Đơn đặt vé không tồn tại!"));

        if (!booking.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("Bạn không có quyền hủy đơn này!");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalArgumentException("Đơn này đã được hủy trước đó!");
        }

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalArgumentException("Chỉ có thể hủy đơn đã xác nhận!");
        }

        if (!booking.getTickets().isEmpty()) {
            Showtime showtime = booking.getTickets().get(0).getShowtime();

            if (showtime.getStartTime().before(Timestamp.valueOf(LocalDateTime.now()))) {
                throw new IllegalArgumentException("Không thể hủy vé cho suất chiếu đã bắt đầu!");
            }

            LocalDateTime deadline = showtime.getStartTime().toLocalDateTime()
                    .minusHours(CANCEL_DEADLINE_HOURS);
            if (LocalDateTime.now().isAfter(deadline)) {
                throw new IllegalArgumentException(
                        "Chỉ có thể hủy vé trước " + CANCEL_DEADLINE_HOURS + " giờ so với giờ chiếu!");
            }
        }

        // Không xóa tickets/products — giữ lại để hiển thị lịch sử
        // Ghế tự động được giải phóng vì query đặt vé filter: booking.status <> 'CANCELLED'
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        log.info("Booking #{} (code: {}) cancelled by user: {}",
                booking.getId(), bookingCode, currentUser.getUsername());
    }

    private BigDecimal calculateSeatPrice(BigDecimal basePrice, SeatType seatType) {
        return switch (seatType) {
            case VIP -> basePrice.multiply(VIP_MULTIPLIER);
            case SWEETBOX -> basePrice.multiply(SWEETBOX_MULTIPLIER);
            default -> basePrice;
        };
    }
}
