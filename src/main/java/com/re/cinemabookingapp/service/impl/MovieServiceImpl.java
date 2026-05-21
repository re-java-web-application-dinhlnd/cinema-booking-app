package com.re.cinemabookingapp.service.impl;

import com.re.cinemabookingapp.dto.tmdb.TmdbMovieDto;
import com.re.cinemabookingapp.entity.Genre;
import com.re.cinemabookingapp.entity.Movie;
import com.re.cinemabookingapp.enums.MovieStatus;
import com.re.cinemabookingapp.dto.movie.MovieUpdateDto;
import com.re.cinemabookingapp.enums.ShowtimeStatus;
import com.re.cinemabookingapp.repository.GenreRepository;
import com.re.cinemabookingapp.repository.MovieRepository;
import com.re.cinemabookingapp.repository.ShowtimeRepository;
import com.re.cinemabookingapp.service.MovieService;
import com.re.cinemabookingapp.service.TmdbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ShowtimeRepository showtimeRepository;
    private final TmdbService tmdbService;

    @Override
    @Transactional
    public Movie importFromTmdb(Long tmdbId) {
        // 1. Kiểm tra trùng
        if (movieRepository.existsByTmdbId(tmdbId)) {
            throw new IllegalArgumentException("Phim này đã có trong hệ thống!");
        }

        // 2. Lấy chi tiết từ TMDB (bao gồm runtime)
        TmdbMovieDto detail = tmdbService.getMovieDetails(tmdbId);
        if (detail == null) {
            throw new IllegalArgumentException("Không tìm thấy phim trên TMDB!");
        }

        // 3. Tạo Movie entity
        Movie movie = new Movie();
        movie.setTmdbId(tmdbId);
        movie.setTitle(detail.getTitle() != null ? detail.getTitle() : detail.getOriginalTitle());
        movie.setDescription(detail.getOverview());
        movie.setDurationMinutes(detail.getRuntime() != null ? detail.getRuntime() : 120);
        movie.setPosterUrl(tmdbService.buildPosterUrl(detail.getPosterPath()));
        movie.setBackdropUrl(tmdbService.buildPosterUrl(detail.getBackdropPath()));
        movie.setVoteAverage(detail.getVoteAverage());

        // Parse release date + auto-detect status
        Date releaseDate = null;
        if (detail.getReleaseDate() != null && !detail.getReleaseDate().isBlank()) {
            try {
                releaseDate = Date.valueOf(detail.getReleaseDate());
                movie.setReleaseDate(releaseDate);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid release date from TMDB: {}", detail.getReleaseDate());
            }
        }

        // Nếu ngày phát hành > hôm nay → COMING_SOON, ngược lại → ACTIVE
        if (releaseDate != null && releaseDate.after(new Date(System.currentTimeMillis()))) {
            movie.setStatus(MovieStatus.COMING_SOON);
        } else {
            movie.setStatus(MovieStatus.ACTIVE);
        }

        // 4. Đồng bộ hóa genres chi tiết từ TMDB
        if (detail.getGenres() != null && !detail.getGenres().isEmpty()) {
            List<Genre> genres = new ArrayList<>();
            for (var tmdbGenre : detail.getGenres()) {
                // Kiểm tra xem thể loại đã tồn tại trong DB chưa
                Genre genre = genreRepository.findByTmdbId(tmdbGenre.getId())
                        .or(() -> genreRepository.findByName(tmdbGenre.getName()))
                        .orElseGet(() -> {
                            // Tạo mới nếu chưa có
                            Genre newGenre = new Genre();
                            newGenre.setTmdbId(tmdbGenre.getId());
                            newGenre.setName(tmdbGenre.getName());
                            return genreRepository.save(newGenre);
                        });
                genres.add(genre);
            }
            movie.setGenres(genres);
        }

        // 5. Lấy danh sách diễn viên chính từ TMDB cast
        if (detail.getCredits() != null && detail.getCredits().getCast() != null && !detail.getCredits().getCast().isEmpty()) {
            // Sắp xếp theo order (thứ tự xuất hiện của diễn viên chính)
            List<String> mainActors = detail.getCredits().getCast().stream()
                    .sorted(Comparator.comparingInt(c -> c.getOrder() != null ? c.getOrder() : 999))
                    .limit(5) // Lấy top 5 diễn viên chính
                    .map(c -> c.getName() != null ? c.getName() : c.getOriginalName())
                    .filter(Objects::nonNull)
                    .toList();
            
            if (!mainActors.isEmpty()) {
                movie.setActors(String.join(", ", mainActors));
            }
        }

        Movie saved = movieRepository.save(movie);
        log.info("Imported movie from TMDB: '{}' (tmdbId={})", saved.getTitle(), tmdbId);
        return saved;
    }

    @Override
    public Page<Movie> searchMovies(String keyword, MovieStatus status, Pageable pageable) {
        return movieRepository.searchMovies(keyword, status, pageable);
    }

    @Override
    public Movie getById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phim với ID: " + id));
    }

    @Override
    @Transactional
    public Movie updateMovie(Long id, MovieUpdateDto dto) {
        Movie movie = getById(id);

        if (dto.getStatus() == MovieStatus.INACTIVE) {
            boolean hasFutureShowtimes = showtimeRepository.existsByMovieIdAndStatusAndStartTimeAfter(
                    id, ShowtimeStatus.ACTIVE, new Timestamp(System.currentTimeMillis()));
            if (hasFutureShowtimes) {
                throw new IllegalArgumentException("Không thể ẩn phim vì phim đang có các suất chiếu hoạt động trong tương lai!");
            }
        }

        movie.setTitle(dto.getTitle());
        movie.setDescription(dto.getDescription());
        movie.setDurationMinutes(dto.getDurationMinutes());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setTrailerUrl(dto.getTrailerUrl());
        movie.setActors(dto.getActors());
        movie.setStatus(dto.getStatus());
        
        log.info("Updated movie: '{}' (id={})", movie.getTitle(), id);
        return movieRepository.save(movie);
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        Movie movie = getById(id);

        boolean hasFutureShowtimes = showtimeRepository.existsByMovieIdAndStatusAndStartTimeAfter(
                id, ShowtimeStatus.ACTIVE, new Timestamp(System.currentTimeMillis()));
        if (hasFutureShowtimes) {
            throw new IllegalArgumentException("Không thể ẩn phim vì phim đang có các suất chiếu hoạt động trong tương lai!");
        }

        movie.setStatus(MovieStatus.INACTIVE);
        movieRepository.save(movie);
        log.info("Soft deleted movie: '{}' (id={})", movie.getTitle(), id);
    }

    @Override
    @Transactional
    public void restore(Long id) {
        Movie movie = getById(id);
        if (movie.getStatus() != MovieStatus.INACTIVE) {
            throw new IllegalArgumentException("Phim này hiện không bị ẩn!");
        }

        // Tự động khôi phục về trạng thái thích hợp dựa trên ngày khởi chiếu
        if (movie.getReleaseDate() != null && movie.getReleaseDate().after(new Date(System.currentTimeMillis()))) {
            movie.setStatus(MovieStatus.COMING_SOON);
        } else {
            movie.setStatus(MovieStatus.ACTIVE);
        }

        movieRepository.save(movie);
        log.info("Restored movie: '{}' (id={}) to status '{}'", movie.getTitle(), id, movie.getStatus());
    }
}
