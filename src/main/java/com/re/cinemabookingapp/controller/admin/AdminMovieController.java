package com.re.cinemabookingapp.controller.admin;

import com.re.cinemabookingapp.dto.tmdb.TmdbMovieDto;
import com.re.cinemabookingapp.dto.tmdb.TmdbSearchResponse;
import com.re.cinemabookingapp.entity.Movie;
import com.re.cinemabookingapp.enums.MovieStatus;
import com.re.cinemabookingapp.repository.MovieRepository;
import com.re.cinemabookingapp.service.MovieService;
import com.re.cinemabookingapp.service.TmdbService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/admin/movies")
@RequiredArgsConstructor
public class AdminMovieController {

    private final MovieService movieService;
    private final TmdbService tmdbService;
    private final MovieRepository movieRepository;

    /**
     * Danh sách phim trong rạp (có search + filter + phân trang)
     */
    @GetMapping
    public String listMovies(@RequestParam(required = false) String keyword,
                             @RequestParam(required = false) String status,
                             @RequestParam(defaultValue = "0") int page,
                             Model model) {

        MovieStatus movieStatus = null;
        if (status != null && !status.isBlank()) {
            try { movieStatus = MovieStatus.valueOf(status); } catch (Exception ignored) {}
        }

        Page<Movie> moviePage = movieService.searchMovies(
                keyword, movieStatus,
                PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "createdAt"))
        );

        model.addAttribute("movies", moviePage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        model.addAttribute("statuses", MovieStatus.values());
        return "admin/movies/list";
    }

    /**
     * Trang tìm kiếm phim từ TMDB
     */
    @GetMapping("/tmdb-search")
    public String tmdbSearchPage() {
        return "admin/movies/tmdb-search";
    }

    /**
     * API AJAX: Tìm kiếm TMDB trả về JSON
     */
    @GetMapping("/api/tmdb-search")
    @ResponseBody
    public ResponseEntity<TmdbSearchResponse> tmdbSearchApi(
            @RequestParam String q,
            @RequestParam(defaultValue = "1") int page) {

        if (q == null || q.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        TmdbSearchResponse response = tmdbService.searchMovies(q.trim(), page);
        if (response == null) {
            response = new TmdbSearchResponse();
            response.setResults(java.util.List.of());
        }
        return ResponseEntity.ok(response);
    }

    /**
     * API AJAX: Duyệt danh sách phim TMDB theo danh mục (now_playing, upcoming, popular)
     */
    @GetMapping("/api/tmdb-browse")
    @ResponseBody
    public ResponseEntity<TmdbSearchResponse> tmdbBrowseApi(
            @RequestParam(defaultValue = "now_playing") String category,
            @RequestParam(defaultValue = "1") int page) {

        TmdbSearchResponse response = switch (category) {
            case "upcoming" -> tmdbService.getUpcoming(page);
            case "popular" -> tmdbService.getPopular(page);
            default -> tmdbService.getNowPlaying(page);
        };

        if (response == null) {
            response = new TmdbSearchResponse();
            response.setResults(java.util.List.of());
        }
        return ResponseEntity.ok(response);
    }

    /**
     * API AJAX: Lấy danh sách tmdbId đã import vào hệ thống
     * → Frontend dùng để disable nút "Thêm vào rạp" cho phim đã có
     */
    @GetMapping("/api/imported-ids")
    @ResponseBody
    public ResponseEntity<Set<Long>> getImportedTmdbIds() {
        return ResponseEntity.ok(movieRepository.findAllTmdbIds());
    }

    /**
     * Import phim từ TMDB vào hệ thống (AJAX JSON response)
     */
    @PostMapping("/import/{tmdbId}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> importFromTmdb(@PathVariable Long tmdbId) {
        Map<String, String> result = new HashMap<>();
        try {
            Movie movie = movieService.importFromTmdb(tmdbId);
            result.put("status", "success");
            result.put("message", "Đã thêm phim \"" + movie.getTitle() + "\" vào hệ thống!");
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", "Lỗi khi import phim: " + e.getMessage());
            return ResponseEntity.internalServerError().body(result);
        }
    }

    /**
     * API AJAX: Lấy chi tiết phim từ TMDB (cho modal xem trước)
     */
    @GetMapping("/api/tmdb-detail/{tmdbId}")
    @ResponseBody
    public ResponseEntity<TmdbMovieDto> tmdbDetailApi(@PathVariable Long tmdbId) {
        TmdbMovieDto detail = tmdbService.getMovieDetails(tmdbId);
        if (detail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detail);
    }

    /**
     * Trang chỉnh sửa phim
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("movie", movieService.getById(id));
        model.addAttribute("statuses", MovieStatus.values());
        return "admin/movies/form";
    }

    /**
     * Xử lý cập nhật phim
     */
    @PostMapping("/{id}")
    public String updateMovie(@PathVariable Long id,
                              @RequestParam String title,
                              @RequestParam String description,
                              @RequestParam Integer durationMinutes,
                              @RequestParam MovieStatus status,
                              RedirectAttributes redirectAttributes) {
        try {
            movieService.updateMovie(id, title, description, durationMinutes, status);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật phim thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/movies";
    }

    /**
     * Xóa mềm phim
     */
    @PostMapping("/{id}/delete")
    public String deleteMovie(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            movieService.softDelete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã ẩn phim thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/movies";
    }
}
