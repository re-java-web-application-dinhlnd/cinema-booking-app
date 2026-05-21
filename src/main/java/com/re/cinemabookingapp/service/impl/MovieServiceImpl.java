package com.re.cinemabookingapp.service.impl;

import com.re.cinemabookingapp.dto.tmdb.TmdbMovieDto;
import com.re.cinemabookingapp.entity.Genre;
import com.re.cinemabookingapp.entity.Movie;
import com.re.cinemabookingapp.enums.MovieStatus;
import com.re.cinemabookingapp.dto.movie.MovieUpdateDto;
import com.re.cinemabookingapp.repository.GenreRepository;
import com.re.cinemabookingapp.repository.MovieRepository;
import com.re.cinemabookingapp.service.MovieService;
import com.re.cinemabookingapp.service.TmdbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
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

        // 4. Map genres từ TMDB genre_ids → local Genre entities
        if (detail.getGenreIds() != null && !detail.getGenreIds().isEmpty()) {
            List<Genre> genres = genreRepository.findByTmdbIdIn(detail.getGenreIds());
            movie.setGenres(genres);
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
        movie.setTitle(dto.getTitle());
        movie.setDescription(dto.getDescription());
        movie.setDurationMinutes(dto.getDurationMinutes());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setTrailerUrl(dto.getTrailerUrl());
        movie.setStatus(dto.getStatus());
        
        log.info("Updated movie: '{}' (id={})", movie.getTitle(), id);
        return movieRepository.save(movie);
    }

    @Override
    @Transactional
    public void softDelete(Long id) {
        Movie movie = getById(id);
        movie.setStatus(MovieStatus.INACTIVE);
        movieRepository.save(movie);
        log.info("Soft deleted movie: '{}' (id={})", movie.getTitle(), id);
    }
}
