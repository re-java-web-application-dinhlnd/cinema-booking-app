package com.re.cinemabookingapp.controller.customer;

import com.re.cinemabookingapp.entity.Movie;
import com.re.cinemabookingapp.enums.MovieStatus;
import com.re.cinemabookingapp.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CustomerController {

    private final MovieRepository movieRepository;

    @GetMapping({"/", "/movies"})
    public String showMovies(@RequestParam(defaultValue = "now") String tab, Model model) {
        MovieStatus status = "coming".equals(tab) ? MovieStatus.COMING_SOON : MovieStatus.ACTIVE;
        List<Movie> movies = movieRepository.findByStatusIn(List.of(status));

        List<Movie> bannerMovies = movieRepository.findByStatusIn(List.of(MovieStatus.ACTIVE))
                .stream()
                .filter(m -> m.getBackdropUrl() != null)
                .limit(6)
                .toList();

        model.addAttribute("movies", movies);
        model.addAttribute("bannerMovies", bannerMovies);
        model.addAttribute("tab", tab);
        return "customer/movies";
    }
}
