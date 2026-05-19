package com.re.cinemabookingapp.dto.profile;

import com.re.cinemabookingapp.validator.PasswordsMatch;
import com.re.cinemabookingapp.validator.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PasswordsMatch
public class ChangePasswordDto {
    
    @NotBlank(message = "Mật khẩu cũ không được để trống")
    private String oldPassword;
    
    @NotBlank(message = "Mật khẩu mới không được để trống")
    @StrongPassword(message = "Mật khẩu phải từ 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt")
    private String newPassword;
    
    @NotBlank(message = "Vui lòng xác nhận mật khẩu mới")
    private String confirmPassword;
}
