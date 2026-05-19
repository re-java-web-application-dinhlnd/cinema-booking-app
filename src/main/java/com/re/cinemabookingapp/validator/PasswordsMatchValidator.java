package com.re.cinemabookingapp.validator;

import com.re.cinemabookingapp.dto.auth.UserRegistrationDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, Object> {

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        if (dto instanceof com.re.cinemabookingapp.dto.auth.UserRegistrationDto) {
            var regDto = (com.re.cinemabookingapp.dto.auth.UserRegistrationDto) dto;
            if (regDto.getPassword() == null || regDto.getConfirmPassword() == null) return false;
            return regDto.getPassword().equals(regDto.getConfirmPassword());
        }

        if (dto instanceof com.re.cinemabookingapp.dto.profile.ChangePasswordDto) {
            var passDto = (com.re.cinemabookingapp.dto.profile.ChangePasswordDto) dto;
            if (passDto.getNewPassword() == null || passDto.getConfirmPassword() == null) return false;
            return passDto.getNewPassword().equals(passDto.getConfirmPassword());
        }

        return false;
    }
}
