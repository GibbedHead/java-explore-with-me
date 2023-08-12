package ru.practicum.explorewithme.ewmservice.event.validator.constraint;

import ru.practicum.explorewithme.ewmservice.event.validator.AtLeastTwoHoursInFutureValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({FIELD, METHOD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastTwoHoursInFutureValidator.class)
@Documented
public @interface AtLeastTwoHoursInFuture {
    String message() default "Value must be at least two hours in the future";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}