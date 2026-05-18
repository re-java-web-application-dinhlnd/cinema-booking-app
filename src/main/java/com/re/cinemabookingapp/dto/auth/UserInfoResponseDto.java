package com.re.cinemabookingapp.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponseDto {
    private String username;
    private String fullName;
    private String email;
    private String role;
}

