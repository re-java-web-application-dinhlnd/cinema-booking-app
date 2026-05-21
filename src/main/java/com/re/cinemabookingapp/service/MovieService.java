package com.re.cinemabookingapp.service;

import com.re.cinemabookingapp.entity.Movie;
import com.re.cinemabookingapp.enums.MovieStatus;
import com.re.cinemabookingapp.dto.movie.MovieUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service quản lý phim trong hệ thống local.
 */
public interface MovieService {

    /** Import phim từ TMDB vào DB local */
    Movie importFromTmdb(Long tmdbId);

    /** Danh sách phim có phân trang + tìm kiếm */
    Page<Movie> searchMovies(String keyword, MovieStatus status, Pageable pageable);

    /** Lấy chi tiết 1 phim */
    Movie getById(Long id);

    /** Cập nhật thông tin phim */
    Movie updateMovie(Long id, MovieUpdateDto dto);

    /** Xóa mềm (ACTIVE → INACTIVE) */
    void softDelete(Long id);

    /** Bỏ ẩn (Khôi phục trạng thái ACTIVE/COMING_SOON) */
    void restore(Long id);
}
