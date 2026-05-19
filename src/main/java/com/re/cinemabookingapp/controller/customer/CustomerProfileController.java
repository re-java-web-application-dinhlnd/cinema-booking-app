package com.re.cinemabookingapp.controller.customer;

import com.re.cinemabookingapp.dto.profile.ChangePasswordDto;
import com.re.cinemabookingapp.dto.profile.UserProfileUpdateDto;
import com.re.cinemabookingapp.entity.UserProfile;
import com.re.cinemabookingapp.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class CustomerProfileController {

    private final ProfileService profileService;

    @GetMapping
    public String showProfilePage(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfile profile = profileService.getUserProfileByUsername(username);

        // Khởi tạo DTO nếu model chưa có (trường hợp load trang bình thường)
        if (!model.containsAttribute("profileDto")) {
            UserProfileUpdateDto profileDto = new UserProfileUpdateDto(
                    profile.getFullName(),
                    profile.getPhoneNumber(),
                    profile.getEmail()
            );
            model.addAttribute("profileDto", profileDto);
        }

        if (!model.containsAttribute("passwordDto")) {
            model.addAttribute("passwordDto", new ChangePasswordDto());
        }

        return "customer/profile";
    }

    @PostMapping("/update")
    public String updateProfileInfo(@Valid @ModelAttribute("profileDto") UserProfileUpdateDto dto,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "profileDto", result);
            redirectAttributes.addFlashAttribute("profileDto", dto);
            return "redirect:/profile";
        }

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            profileService.updateProfileInfo(username, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin cá nhân thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }
        
        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("passwordDto") ChangePasswordDto dto,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
                                     
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "passwordDto", result);
            redirectAttributes.addFlashAttribute("passwordDto", dto);
            redirectAttributes.addFlashAttribute("activeTab", "password"); // Để JS tự động bật tab Mật khẩu
            return "redirect:/profile";
        }

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            profileService.changeUserPassword(username, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công!");
        } catch (IllegalArgumentException e) {
            result.rejectValue(e.getMessage().contains("Mật khẩu cũ") ? "oldPassword" : "confirmPassword", "error.passwordDto", e.getMessage());
            redirectAttributes.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "passwordDto", result);
            redirectAttributes.addFlashAttribute("passwordDto", dto);
            redirectAttributes.addFlashAttribute("activeTab", "password");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/profile";
    }
}
