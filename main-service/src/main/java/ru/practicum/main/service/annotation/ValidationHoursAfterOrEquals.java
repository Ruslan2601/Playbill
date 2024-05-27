package ru.practicum.main.service.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class ValidationHoursAfterOrEquals implements ConstraintValidator<HoursAfterOrEquals, LocalDateTime> {

    private int hours;

    @Override
    public void initialize(HoursAfterOrEquals constraintAnnotation) {
        hours = constraintAnnotation.hours();
    }

    @Override
    public boolean isValid(LocalDateTime date, ConstraintValidatorContext context) {
        if (date == null) {
            return true;
        }

        return !LocalDateTime.now().plusHours(hours).isAfter(date);
    }
}
