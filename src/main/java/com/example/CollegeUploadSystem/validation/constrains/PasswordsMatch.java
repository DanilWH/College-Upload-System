package com.example.CollegeUploadSystem.validation.constrains;

import com.example.CollegeUploadSystem.validation.validators.PasswordsMatchConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordsMatchConstraintValidator.class)
public @interface PasswordsMatch {
    String message() default "Пароли не совпадают";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
