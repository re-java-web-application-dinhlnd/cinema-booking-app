package com.re.cinemabookingapp.validator;

import com.re.cinemabookingapp.dto.auth.UserRegistrationDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, UserRegistrationDto> {

    @Override
    public boolean isValid(UserRegistrationDto dto, ConstraintValidatorContext context) {
        if(dto.getPassword() == null || dto.getConfirmPassword() == null)  return false;

        return dto.getPassword().equals(dto.getConfirmPassword());
    }
}
