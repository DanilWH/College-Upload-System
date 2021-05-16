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

        // if the password's value is null, that means the user doesn't want to change the password
        // when editing its profile, so we just skip the password validation if the password is null.
        if (user.getPassword() == null) {
            return true;
        }

        return user.getPassword().equals(user.getConfirmPassword());
    }
}
