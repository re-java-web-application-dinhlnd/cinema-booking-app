package com.re.cinemabookingapp.controller.admin;

import com.re.cinemabookingapp.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("stats", dashboardService.getDashboardStats());
        return "admin/dashboard";
    }
}
