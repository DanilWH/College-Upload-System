package com.example.CollegeUploadSystem.validation.constrains;

import com.example.CollegeUploadSystem.validation.validators.LoginConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({ FIELD, PARAMETER, METHOD})
@Retention(RUNTIME)
@Constraint(validatedBy = LoginConstraintValidator.class)
public @interface ValidLogin {
    String message() default "Некорректный логин";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
