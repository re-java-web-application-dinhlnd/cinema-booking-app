package com.re.cinemabookingapp.controller.advice;

import com.re.cinemabookingapp.enums.MovieStatus;
import com.re.cinemabookingapp.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice(basePackages = "com.re.cinemabookingapp.controller.customer")
@RequiredArgsConstructor
public class NavModelAdvice {

    private final MovieRepository movieRepository;

    @ModelAttribute
    public void addNavMovies(Model model) {
        model.addAttribute("navNowShowing",
                movieRepository.findByStatus(MovieStatus.ACTIVE, PageRequest.of(0, 4)).getContent());
        model.addAttribute("navComingSoon",
                movieRepository.findByStatus(MovieStatus.COMING_SOON, PageRequest.of(0, 4)).getContent());
    }
}
