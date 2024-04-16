package com.example.nnui_sem_prace.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class VekValidator implements ConstraintValidator<VekConstraint, LocalDate> {

    @Override
    public void initialize(VekConstraint constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext constraintValidatorContext) {
        if (dateOfBirth == null) {
            return false;
        }
        LocalDate now = LocalDate.now();
        return dateOfBirth.plusYears(18).isBefore(now);
    }
}
