package com.example.CollegeUploadSystem.validation.validators;

import com.example.CollegeUploadSystem.dto.input.ProfilePasswordInput;
import com.example.CollegeUploadSystem.validation.constrains.PasswordsMatch;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordsMatchConstraintValidator implements ConstraintValidator<PasswordsMatch, Object> {
    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        ProfilePasswordInput profilePasswordInput = (ProfilePasswordInput) object;

        // throw the 400 Bad request error if the password is null.
        if (profilePasswordInput.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The new password must not be null");
        }

        return profilePasswordInput.getPassword().equals(profilePasswordInput.getConfirmPassword());
    }
}
