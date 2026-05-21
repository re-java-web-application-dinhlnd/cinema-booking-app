package com.re.cinemabookingapp.service.impl;

import com.re.cinemabookingapp.configuration.TmdbProperties;
import com.re.cinemabookingapp.dto.tmdb.TmdbMovieDto;
import com.re.cinemabookingapp.dto.tmdb.TmdbSearchResponse;
import com.re.cinemabookingapp.service.TmdbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * Implementation gọi TMDB API v3 qua RestClient.
 * Sử dụng api_key query parameter để xác thực.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TmdbServiceImpl implements TmdbService {

    private final RestClient tmdbRestClient;
    private final TmdbProperties tmdbProperties;

    @Override
    public TmdbSearchResponse searchMovies(String query, int page) {
        log.info("TMDB Search: query='{}', page={}, apiKey={}...", query, page,
                tmdbProperties.getApiKey() != null ? tmdbProperties.getApiKey().substring(0, Math.min(8, tmdbProperties.getApiKey().length())) : "NULL");

        try {
            TmdbSearchResponse response = tmdbRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/movie")
                            .queryParam("api_key", tmdbProperties.getApiKey())
                            .queryParam("query", query)
                            .queryParam("language", "vi-VN")
                            .queryParam("page", page)
                            .queryParam("include_adult", false)
                            .build())
                    .retrieve()
                    .body(TmdbSearchResponse.class);

            if (response != null) {
                log.info("TMDB Search: found {} results", response.getTotalResults());
            }

            return response;
        } catch (Exception e) {
            log.error("TMDB Search failed: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public TmdbMovieDto getMovieDetails(Long tmdbId) {
        log.info("TMDB Details: tmdbId={}", tmdbId);

        try {
            return tmdbRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movie/{id}")
                            .queryParam("api_key", tmdbProperties.getApiKey())
                            .queryParam("language", "vi-VN")
                            .queryParam("append_to_response", "videos,credits")
                            .build(tmdbId))
                    .retrieve()
                    .body(TmdbMovieDto.class);
        } catch (Exception e) {
            log.error("TMDB Details failed for tmdbId={}: {}", tmdbId, e.getMessage());
            return null;
        }
    }

    @Override
    public TmdbSearchResponse getNowPlaying(int page) {
        return fetchMovieList("/movie/now_playing", page);
    }

    @Override
    public TmdbSearchResponse getUpcoming(int page) {
        return fetchMovieList("/movie/upcoming", page);
    }

    @Override
    public TmdbSearchResponse getPopular(int page) {
        return fetchMovieList("/movie/popular", page);
    }

    /**
     * Helper: gọi TMDB list endpoint (now_playing, upcoming, popular)
     */
    private TmdbSearchResponse fetchMovieList(String path, int page) {
        log.info("TMDB List: path='{}', page={}", path, page);
        try {
            TmdbSearchResponse response = tmdbRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(path)
                            .queryParam("api_key", tmdbProperties.getApiKey())
                            .queryParam("language", "vi-VN")
                            .queryParam("region", "VN")
                            .queryParam("page", page)
                            .build())
                    .retrieve()
                    .body(TmdbSearchResponse.class);

            if (response != null) {
                log.info("TMDB List '{}': {} results", path, response.getTotalResults());
            }
            return response;
        } catch (Exception e) {
            log.error("TMDB List '{}' failed: {}", path, e.getMessage());
            return null;
        }
    }

    @Override
    public String buildPosterUrl(String posterPath) {
        if (posterPath == null || posterPath.isBlank()) {
            return null;
        }
        return tmdbProperties.getImageBaseUrl() + posterPath;
    }
}
