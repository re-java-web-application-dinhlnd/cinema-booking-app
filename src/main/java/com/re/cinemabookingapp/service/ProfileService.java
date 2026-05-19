package com.re.cinemabookingapp.service;

import com.re.cinemabookingapp.dto.profile.ChangePasswordDto;
import com.re.cinemabookingapp.dto.profile.UserProfileUpdateDto;
import com.re.cinemabookingapp.entity.User;
import com.re.cinemabookingapp.entity.UserProfile;

public interface ProfileService {
    
    UserProfile getUserProfileByUsername(String username);
    
    User getUserByUsername(String username);

    void updateProfileInfo(String username, UserProfileUpdateDto dto);

    void changeUserPassword(String username, ChangePasswordDto dto);
}
