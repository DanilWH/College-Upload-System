package com.example.CollegeUploadSystem.validation.validators;

import com.example.CollegeUploadSystem.services.UserService;
import com.example.CollegeUploadSystem.validation.constrains.ValidLogin;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginConstraintValidator implements ConstraintValidator<ValidLogin, String> {

    @Autowired
    private UserService userService;

    private final String LOGIN_PATTERN = "[^A-Za-zА-Яа-я0-9@_\\-+./]";

    @Override
    public void initialize(ValidLogin constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * <p>Checks if the login of a user matches the regex pattern.
     * It also check if the login already exists.</p>
     *
     * @param  login
     *         The annotated field
     *
     * @param  context
     *         is put into the BindingResult object in a controller.
     *
     * @return true - the annotation passed the value
     *         false - the annotation didn't pass the value
     */
    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        // preparation for regex comparing.
        Pattern pattern = Pattern.compile(this.LOGIN_PATTERN);
        Matcher matcher = pattern.matcher(login);

        String messageTemplate = null;

        // check if the user login satisfies the regex requirements.
        if (matcher.find()) {
            messageTemplate = "Логин '" + login + "' не соответствует требованиям";
        }

        // check if the user login satisfies the length rule.
        int loginLength = login.length();
        if (loginLength < 3) {
            messageTemplate = "Логин должен быть больше 3-х символов в длину";
        } else if (loginLength > 20) {
            messageTemplate = "Логин должен быть не более 20-ти символов в длину";
        }

        // the "The login ... already exists" rule is checked in the controller because of the inability to
        // put the current user session in the constraint validator and compare if the user has changed its
        // login or not so we know if we should check the user's new login existence in the database.

        // add a custom message into constraint violation if any of discrepancy appeared.
        if (messageTemplate != null) {
            context.buildConstraintViolationWithTemplate(messageTemplate)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();

            return false;
        }

        return true;
    }
}
