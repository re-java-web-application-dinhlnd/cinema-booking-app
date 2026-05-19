package com.re.cinemabookingapp.controller.staff;

import com.re.cinemabookingapp.dto.profile.ChangePasswordDto;
import com.re.cinemabookingapp.dto.profile.UserProfileUpdateDto;
import com.re.cinemabookingapp.entity.UserProfile;
import com.re.cinemabookingapp.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/pos/profile")
@RequiredArgsConstructor
public class StaffProfileController {

    private final ProfileService profileService;

    @GetMapping
    public String showProfilePage(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfile profile = profileService.getUserProfileByUsername(username);

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

        model.addAttribute("username", username);
        return "staff/profile";
    }

    /**
     * Helper: chuẩn bị model attributes chung trước khi render lại trang profile
     */
    private void prepareModelForRender(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);
    }

    @PostMapping("/update")
    public String updateProfileInfo(@Valid @ModelAttribute("profileDto") UserProfileUpdateDto dto,
                                    BindingResult result,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            // Render trực tiếp — giữ nguyên dữ liệu form, không redirect
            model.addAttribute("passwordDto", new ChangePasswordDto());
            prepareModelForRender(model);
            return "staff/profile";
        }

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            profileService.updateProfileInfo(username, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin cá nhân thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/pos/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("passwordDto") ChangePasswordDto dto,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            // Render trực tiếp — giữ nguyên dữ liệu form, tự động bật tab Mật khẩu
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            UserProfile profile = profileService.getUserProfileByUsername(username);
            model.addAttribute("profileDto", new UserProfileUpdateDto(
                    profile.getFullName(), profile.getPhoneNumber(), profile.getEmail()
            ));
            model.addAttribute("activeTab", "password");
            prepareModelForRender(model);
            return "staff/profile";
        }

        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            profileService.changeUserPassword(username, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Đổi mật khẩu thành công!");
        } catch (IllegalArgumentException e) {
            // Lỗi business logic — render trực tiếp để giữ form data
            result.rejectValue(
                    e.getMessage().contains("Mật khẩu cũ") ? "oldPassword" : "confirmPassword",
                    "error.passwordDto", e.getMessage()
            );
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            UserProfile profile = profileService.getUserProfileByUsername(username);
            model.addAttribute("profileDto", new UserProfileUpdateDto(
                    profile.getFullName(), profile.getPhoneNumber(), profile.getEmail()
            ));
            model.addAttribute("activeTab", "password");
            prepareModelForRender(model);
            return "staff/profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
        }

        return "redirect:/pos/profile";
    }
}
