package com.re.cinemabookingapp.controller;

import com.re.cinemabookingapp.dto.auth.UserRegistrationDto;
import com.re.cinemabookingapp.entity.User;
import com.re.cinemabookingapp.entity.UserProfile;
import com.re.cinemabookingapp.enums.UserRole;
import com.re.cinemabookingapp.enums.UserStatus;
import com.re.cinemabookingapp.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/admin/login")
    public String adminLoginPage(){
        return "admin/login";
    }

    @PostMapping("/api/auth/register")
    @ResponseBody
    public ResponseEntity<?> registerCustomer(
            @Valid @RequestBody UserRegistrationDto dto,
            BindingResult bindingResult
            ){
        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();

            for(ObjectError error: bindingResult.getAllErrors()){
                if(error instanceof FieldError fieldError){

                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
                else {
                    errors.put("globalError", error.getDefaultMessage());
                }
            }

            return ResponseEntity.badRequest().body(Map.of("success", false, "errors", errors));
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Username đã tồn tại!"));
        }
        if (userRepository.existsByUserProfileEmail(dto.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Email đã tồn tại!"));
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);

        UserProfile profile = new UserProfile();
        profile.setFullName(dto.getFullName());
        profile.setEmail(dto.getEmail());
        profile.setPhoneNumber(dto.getPhoneNumber());
        profile.setUser(user);

        user.setUserProfile(profile);
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("success", true, "message", "Đăng ký thành công!"));
    }
}
