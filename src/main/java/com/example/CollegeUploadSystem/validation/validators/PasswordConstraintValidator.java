package com.example.CollegeUploadSystem.validation.validators;


import com.example.CollegeUploadSystem.validation.constrains.ValidPassword;
import org.passay.*;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("src/main/resources/passay.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MessageResolver resolver = new PropertiesMessageResolver(properties);

        PasswordValidator validator = new PasswordValidator(resolver, Arrays.asList(
                // the length must be between 8 and 20 numbers of symbols.
                new LengthRule(8, 20),

                // at least one english character.
                new CharacterRule(EnglishCharacterData.LowerCase, 1),

                // at least one digit.
                new CharacterRule(EnglishCharacterData.Digit, 1),

                // no alphabetical sequences.
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 4, false),

                // no numerical sequences.
                new IllegalSequenceRule(EnglishSequenceData.Numerical, 4, false),

                // no white spaces.
                new WhitespaceRule()
        ));

        RuleResult ruleResult = validator.validate(new PasswordData(password));

        if (ruleResult.isValid()) {
            return true;
        }

        List<String> messages = validator.getMessages(ruleResult);
        String messageTemplate = String.join("<br>", messages);

        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();

        return false;
    }
}
