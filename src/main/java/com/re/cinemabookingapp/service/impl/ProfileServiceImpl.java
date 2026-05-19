package com.re.cinemabookingapp.service.impl;

import com.re.cinemabookingapp.dto.profile.ChangePasswordDto;
import com.re.cinemabookingapp.dto.profile.UserProfileUpdateDto;
import com.re.cinemabookingapp.entity.User;
import com.re.cinemabookingapp.entity.UserProfile;
import com.re.cinemabookingapp.repository.UserProfileRepository;
import com.re.cinemabookingapp.repository.UserRepository;
import com.re.cinemabookingapp.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserProfile getUserProfileByUsername(String username) {
        User user = getUserByUsername(username);
        return userProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    @Transactional
    public void updateProfileInfo(String username, UserProfileUpdateDto dto) {
        UserProfile profile = getUserProfileByUsername(username);
        profile.setFullName(dto.getFullName());
        profile.setPhoneNumber(dto.getPhoneNumber());
        
        // Cập nhật thông tin UserProfile
        userProfileRepository.save(profile);
    }

    @Override
    @Transactional
    public void changeUserPassword(String username, ChangePasswordDto dto) {
        User user = getUserByUsername(username);
        
        // Mật khẩu cũ không khớp
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu cũ không chính xác");
        }
        
        // Xác nhận mật khẩu không khớp
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("Xác nhận mật khẩu không khớp");
        }
        
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}
