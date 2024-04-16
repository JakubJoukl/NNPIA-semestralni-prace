package com.example.nnui_sem_prace.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VekValidator.class)
@Documented
public @interface VekConstraint {
    String message() default "Pacienti musí být starší 18 let";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
