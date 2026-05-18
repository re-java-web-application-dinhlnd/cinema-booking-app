package com.re.cinemabookingapp.dto.auth;

import com.re.cinemabookingapp.validator.PasswordsMatch;
import com.re.cinemabookingapp.validator.StrongPassword;
import com.re.cinemabookingapp.validator.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@PasswordsMatch
public class UserRegistrationDto {
    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String username;

    @StrongPassword
    private String password;

    @NotBlank(message = "Vui lòng nhập lại mật khẩu")
    private String confirmPassword;

    @NotBlank(message = "Họ tên không được để trống")
    private String fullName;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @ValidPhoneNumber
    private String phoneNumber;
}
