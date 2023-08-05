package ru.practicum.explorewithme.ewmservice.event.validator;

import ru.practicum.explorewithme.ewmservice.event.validator.constraint.AtLeastTwoHoursInFuture;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class AtLeastOneHourInFutureValidator implements ConstraintValidator<AtLeastTwoHoursInFuture, LocalDateTime> {
    @Override
    public final boolean isValid(final LocalDateTime value,
                                 final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.isAfter(LocalDateTime.now().plusHours(1)) || value.isEqual(LocalDateTime.now().plusHours(1));
    }
}
