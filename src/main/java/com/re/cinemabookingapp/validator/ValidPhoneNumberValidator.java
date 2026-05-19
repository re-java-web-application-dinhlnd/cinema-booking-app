package com.re.cinemabookingapp.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidPhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if(phoneNumber == null || phoneNumber.isBlank())    return true;

        return phoneNumber.matches("^(84|0[35789])\\d{8}$");
    }
}
