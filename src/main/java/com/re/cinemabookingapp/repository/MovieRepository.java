package com.re.cinemabookingapp.repository;

import com.re.cinemabookingapp.entity.Movie;
import com.re.cinemabookingapp.enums.MovieStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    boolean existsByTmdbId(Long tmdbId);

    /** Lấy tất cả tmdbId đã import → dùng để đánh dấu phim đã thêm trên trang TMDB */
    @Query("SELECT m.tmdbId FROM Movie m WHERE m.tmdbId IS NOT NULL")
    Set<Long> findAllTmdbIds();

    Page<Movie> findByStatus(MovieStatus status, Pageable pageable);

    /** Lấy danh sách phim theo nhiều trạng thái (cho dropdown chọn phim) */
    List<Movie> findByStatusIn(List<MovieStatus> statuses);

    @Query("SELECT m FROM Movie m WHERE " +
           "(:keyword IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (:status IS NULL OR m.status = :status)")
    Page<Movie> searchMovies(@Param("keyword") String keyword,
                             @Param("status") MovieStatus status,
                             Pageable pageable);

    long countByStatus(MovieStatus status);
}
