package com.re.cinemabookingapp.controller.admin;

import com.re.cinemabookingapp.dto.showtime.ShowtimeCreateDto;
import com.re.cinemabookingapp.dto.showtime.ShowtimeUpdateDto;
import com.re.cinemabookingapp.entity.Showtime;
import com.re.cinemabookingapp.enums.MovieStatus;
import com.re.cinemabookingapp.enums.RoomStatus;
import com.re.cinemabookingapp.enums.ShowtimeStatus;
import com.re.cinemabookingapp.repository.MovieRepository;
import com.re.cinemabookingapp.repository.RoomRepository;
import com.re.cinemabookingapp.repository.ShowtimeRepository;
import com.re.cinemabookingapp.service.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/showtimes")
@RequiredArgsConstructor
public class AdminShowtimeController {

    private final ShowtimeService showtimeService;
    private final MovieRepository movieRepository;
    private final RoomRepository roomRepository;
    private final ShowtimeRepository showtimeRepository;

    /**
     * Danh sách suất chiếu có filter + phân trang.
     */
    @GetMapping
    public String listShowtimes(@RequestParam(required = false) Long movieId,
                                @RequestParam(required = false) Long roomId,
                                @RequestParam(required = false) String status,
                                @RequestParam(defaultValue = "0") int page,
                                Model model) {

        ShowtimeStatus showtimeStatus = null;
        if (status != null && !status.isBlank()) {
            try {
                showtimeStatus = ShowtimeStatus.valueOf(status);
            } catch (IllegalArgumentException ignored) {}
        }

        Page<Showtime> showtimes = showtimeService.search(
                movieId, roomId, showtimeStatus,
                PageRequest.of(page, 10, Sort.by("startTime").descending()));

        model.addAttribute("showtimes", showtimes);
        model.addAttribute("movies", movieRepository.findByStatusIn(
                List.of(MovieStatus.ACTIVE, MovieStatus.COMING_SOON)));
        model.addAttribute("rooms", roomRepository.findAllByStatus(RoomStatus.ACTIVE));
        model.addAttribute("statuses", ShowtimeStatus.values());

        // Giữ lại giá trị filter trên form
        model.addAttribute("selectedMovieId", movieId);
        model.addAttribute("selectedRoomId", roomId);
        model.addAttribute("selectedStatus", status);

        return "admin/showtimes/list";
    }

    /**
     * Trang tạo suất chiếu mới.
     */
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("movies", movieRepository.findByStatusIn(
                List.of(MovieStatus.ACTIVE, MovieStatus.COMING_SOON)));
        model.addAttribute("rooms", roomRepository.findAllByStatus(RoomStatus.ACTIVE));
        return "admin/showtimes/form";
    }

    /**
     * Xử lý tạo suất chiếu mới.
     */
    @PostMapping("/create")
    public String createShowtime(@Valid @ModelAttribute("dto") ShowtimeCreateDto dto,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        // Backend validation — DTO constraints
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(e -> fieldErrors.put(e.getField(), e.getDefaultMessage()));
            model.addAttribute("fieldErrors", fieldErrors);
            model.addAttribute("movies", movieRepository.findByStatusIn(
                    List.of(MovieStatus.ACTIVE, MovieStatus.COMING_SOON)));
            model.addAttribute("rooms", roomRepository.findAllByStatus(RoomStatus.ACTIVE));
            return "admin/showtimes/form";
        }
        try {
            showtimeService.create(dto);
            redirectAttributes.addFlashAttribute("successMessage", "Tạo suất chiếu thành công!");
            return "redirect:/admin/showtimes";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("movies", movieRepository.findByStatusIn(
                    List.of(MovieStatus.ACTIVE, MovieStatus.COMING_SOON)));
            model.addAttribute("rooms", roomRepository.findAllByStatus(RoomStatus.ACTIVE));
            return "admin/showtimes/form";
        }
    }

    /**
     * Trang chỉnh sửa suất chiếu.
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Showtime showtime = showtimeService.getById(id);
        model.addAttribute("showtime", showtime);
        model.addAttribute("statuses", ShowtimeStatus.values());
        model.addAttribute("isEdit", true);
        return "admin/showtimes/form";
    }

    /**
     * Xử lý cập nhật suất chiếu.
     */
    @PostMapping("/{id}/edit")
    public String updateShowtime(@PathVariable Long id,
                                 @Valid @ModelAttribute ShowtimeUpdateDto dto,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        Showtime showtime = showtimeService.getById(id);

        // Backend validation — DTO constraints
        if (bindingResult.hasErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(e -> fieldErrors.put(e.getField(), e.getDefaultMessage()));
            model.addAttribute("fieldErrors", fieldErrors);
            model.addAttribute("showtime", showtime);
            model.addAttribute("statuses", ShowtimeStatus.values());
            model.addAttribute("isEdit", true);
            return "admin/showtimes/form";
        }
        try {
            showtimeService.update(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật suất chiếu thành công!");
            return "redirect:/admin/showtimes";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("showtime", showtime);
            model.addAttribute("statuses", ShowtimeStatus.values());
            model.addAttribute("isEdit", true);
            return "admin/showtimes/form";
        }
    }

    /**
     * Ẩn suất chiếu (soft hide).
     */
    @PostMapping("/{id}/hide")
    public String hideShowtime(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            showtimeService.softHide(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã ẩn suất chiếu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/showtimes";
    }

    /**
     * AJAX: Lấy lịch suất chiếu của 1 phòng trong 1 ngày.
     * Dùng để hiển thị hint cho Admin khi tạo suất chiếu mới.
     */
    @GetMapping("/api/room-schedule")
    @ResponseBody
    public ResponseEntity<List<Map<String, String>>> getRoomSchedule(
            @RequestParam Long roomId,
            @RequestParam String date) {

        LocalDate localDate = LocalDate.parse(date);
        Timestamp dayStart = Timestamp.valueOf(localDate.atStartOfDay());
        Timestamp dayEnd = Timestamp.valueOf(localDate.plusDays(1).atStartOfDay());

        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

        List<Showtime> showtimes = showtimeRepository.findConflicting(roomId, dayStart, dayEnd);

        List<Map<String, String>> result = showtimes.stream()
                .sorted((a, b) -> a.getStartTime().compareTo(b.getStartTime()))
                .map(st -> {
                    Map<String, String> item = new HashMap<>();
                    item.put("movieTitle", st.getMovie().getTitle());
                    item.put("startTime", st.getStartTime().toLocalDateTime().format(timeFmt));
                    item.put("endTime", st.getEndTime().toLocalDateTime().format(timeFmt));
                    return item;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}

