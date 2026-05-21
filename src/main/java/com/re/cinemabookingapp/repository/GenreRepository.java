package com.re.cinemabookingapp.repository;

import com.re.cinemabookingapp.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findByTmdbIdIn(List<Long> tmdbIds);
   Optional<Genre> findByTmdbId(Long tmdbId);
   Optional<Genre> findByName(String name);
}
