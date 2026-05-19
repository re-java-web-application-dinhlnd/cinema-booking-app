package com.re.cinemabookingapp.dto.profile;

import com.re.cinemabookingapp.validator.ValidPhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateDto {
    
    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;
    
    @ValidPhoneNumber
    private String phoneNumber;

    private String email;
}
