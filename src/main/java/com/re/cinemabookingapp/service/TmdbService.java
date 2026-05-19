package com.re.cinemabookingapp.service;

import com.re.cinemabookingapp.dto.tmdb.TmdbMovieDto;
import com.re.cinemabookingapp.dto.tmdb.TmdbSearchResponse;

/**
 * Service gọi TMDB API bên ngoài.
 */
public interface TmdbService {

    TmdbSearchResponse searchMovies(String query, int page);

    TmdbSearchResponse getNowPlaying(int page);

    TmdbSearchResponse getUpcoming(int page);

    TmdbSearchResponse getPopular(int page);

    TmdbMovieDto getMovieDetails(Long tmdbId);

    /**
     * Tạo URL poster đầy đủ từ poster_path.
     */
    String buildPosterUrl(String posterPath);
}
