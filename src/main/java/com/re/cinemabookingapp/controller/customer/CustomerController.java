package com.re.cinemabookingapp.controller.customer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class CustomerController {
    @GetMapping
    public String showHome(){
        return "index";
    }
}
