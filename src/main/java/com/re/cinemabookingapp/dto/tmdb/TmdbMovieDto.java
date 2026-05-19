package com.re.cinemabookingapp.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO mapping thông tin 1 bộ phim từ TMDB API.
 * Dùng cho cả Search results và Movie Details response.
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TmdbMovieDto {

    /** TMDB movie ID */
    private Long id;

    private String title;

    @JsonProperty("original_title")
    private String originalTitle;

    /** Mô tả phim (tiếng Việt nếu có) */
    private String overview;

    /** Đường dẫn poster (cần ghép thêm image base URL) */
    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("backdrop_path")
    private String backdropPath;

    @JsonProperty("release_date")
    private String releaseDate;

    /** Danh sách genre IDs (chỉ có trong Search, không có trong Details) */
    @JsonProperty("genre_ids")
    private List<Long> genreIds;

    /** Thời lượng phim (phút) — chỉ có trong Movie Details, không có trong Search */
    private Integer runtime;

    @JsonProperty("vote_average")
    private Double voteAverage;

    @JsonProperty("vote_count")
    private Integer voteCount;

    private Double popularity;

    /** Tagline ngắn của phim — chỉ có trong Movie Details */
    private String tagline;

    /** Danh sách genre chi tiết (id + name) — chỉ có trong Movie Details */
    private List<TmdbGenreDto> genres;

    /** Videos (trailers) — chỉ có khi dùng append_to_response=videos */
    private TmdbVideosWrapper videos;

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TmdbGenreDto {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TmdbVideosWrapper {
        private List<TmdbVideoDto> results;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TmdbVideoDto {
        private String key;       // YouTube video ID
        private String name;      // Tên video (VD: "Official Trailer")
        private String site;      // "YouTube"
        private String type;      // "Trailer", "Teaser", "Featurette"...
        private Boolean official;  // Trailer chính thức?
    }
}
