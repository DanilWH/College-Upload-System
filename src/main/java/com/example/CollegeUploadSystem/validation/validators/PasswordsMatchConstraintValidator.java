package com.example.CollegeUploadSystem.validation.validators;

import com.example.CollegeUploadSystem.models.User;
import com.example.CollegeUploadSystem.validation.constrains.PasswordsMatch;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsMatchConstraintValidator implements ConstraintValidator<PasswordsMatch, Object> {
    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        User user = (User) object;
        return user.getPassword().equals(user.getConfirmPassword());
    }
}
