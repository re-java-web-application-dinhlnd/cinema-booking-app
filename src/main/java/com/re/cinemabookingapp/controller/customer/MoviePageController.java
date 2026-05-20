package com.re.cinemabookingapp.controller.customer;

import com.re.cinemabookingapp.entity.Movie;
import com.re.cinemabookingapp.entity.Showtime;
import com.re.cinemabookingapp.enums.MovieStatus;
import com.re.cinemabookingapp.repository.MovieRepository;
import com.re.cinemabookingapp.repository.ShowtimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MoviePageController {

    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;

    @GetMapping("/movies")
    public String moviesPage(@RequestParam(defaultValue = "now") String tab, Model model) {
        MovieStatus status = "coming".equals(tab) ? MovieStatus.COMING_SOON : MovieStatus.ACTIVE;
        List<Movie> movies = movieRepository.findByStatusIn(List.of(status));
        model.addAttribute("movies", movies);
        model.addAttribute("tab", tab);
        return "customer/movies";
    }

    @GetMapping("/movies/{id}")
    public String movieDetail(@PathVariable Long id, Model model) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Phim không tồn tại!"));

        LocalDate today = LocalDate.now();
        Timestamp dayStart = Timestamp.valueOf(today.atStartOfDay());
        Timestamp dayEnd = Timestamp.valueOf(today.plusDays(1).atStartOfDay());

        List<Showtime> showtimes = showtimeRepository.findActiveByMovieAndDate(id, dayStart, dayEnd)
                .stream()
                .filter(s -> s.getStartTime().toLocalDateTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        model.addAttribute("movie", movie);
        model.addAttribute("showtimes", showtimes);
        return "customer/movie-detail";
    }

    @GetMapping("/api/showtimes")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getShowtimes(
            @RequestParam Long movieId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        Timestamp dayStart = Timestamp.valueOf(date.atStartOfDay());
        Timestamp dayEnd = Timestamp.valueOf(date.plusDays(1).atStartOfDay());
        List<Showtime> showtimes = showtimeRepository.findActiveByMovieAndDate(movieId, dayStart, dayEnd);

        boolean isToday = date.equals(LocalDate.now());
        List<Map<String, Object>> items = showtimes.stream()
                .filter(s -> !isToday || s.getStartTime().toLocalDateTime().isAfter(LocalDateTime.now()))
                .map(s -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", s.getId());
                    m.put("startTime", s.getStartTime().toLocalDateTime().toLocalTime().toString().substring(0, 5));
                    return m;
                })
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("showtimes", items);
        return ResponseEntity.ok(response);
    }
}
