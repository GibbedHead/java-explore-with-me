package ru.practicum.explorewithme.ewmservice.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.ewmservice.exception.model.EntityNotFoundException;
import ru.practicum.explorewithme.ewmservice.exception.model.ExceptionResponseEntity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.StringJoiner;

@RestControllerAdvice("ru.practicum.explorewithme")
public class ErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResponseEntity handleValidationErrors(MethodArgumentNotValidException ex) {
        StringJoiner message = new StringJoiner(". ");
        BindingResult bindingResult = ex.getBindingResult();
        if (bindingResult.hasErrors()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                StringJoiner fieldMessage = new StringJoiner("; ");
                fieldMessage.add("Field: ".concat(fieldError.getField()));
                fieldMessage.add("Error: ".concat(
                                Objects.requireNonNull(
                                        fieldError.getDefaultMessage(),
                                        "validation error"
                                )
                        )
                );
                String rejectedValue;
                rejectedValue = (fieldError.getRejectedValue() == null)
                        ? "null"
                        : fieldError.getRejectedValue().toString();
                fieldMessage.add("Value: `".concat(rejectedValue).concat("`"));
                message.add(fieldMessage.toString());
            }
        }
        return new ExceptionResponseEntity(
                HttpStatus.BAD_REQUEST.toString(),
                "Incorrectly made request.",
                message.toString(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ExceptionResponseEntity handleValidationErrors(ConstraintViolationException ex) {
        return new ExceptionResponseEntity(
                HttpStatus.CONFLICT.toString(),
                "Integrity constraint has been violated.",
                ex.getCause().getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ExceptionResponseEntity handleValidationErrors(EntityNotFoundException ex) {
        return new ExceptionResponseEntity(
                HttpStatus.NOT_FOUND.toString(),
                "The required object was not found.",
                ex.getMessage(),
                LocalDateTime.now()
        );
    }
}
