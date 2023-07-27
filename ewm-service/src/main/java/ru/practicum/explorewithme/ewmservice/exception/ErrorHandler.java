package ru.practicum.explorewithme.ewmservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
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
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            StringJoiner fieldMessage = new StringJoiner("; ");
            fieldMessage.add("Field: ".concat(fieldError.getField()));
            fieldMessage.add("Error: ".concat(
                            Objects.requireNonNull(
                                    fieldError.getDefaultMessage(),
                                    "validation error"
                            )
                    )
            );
            fieldMessage.add("Value: `".concat(
                            Objects.requireNonNull(
                                            fieldError.getRejectedValue(),
                                            "default value"
                                    ).toString()
                                    .concat("`")
                    )
            );
            message.add(fieldMessage.toString());
        }
        return new ExceptionResponseEntity(
                HttpStatus.BAD_REQUEST.toString(),
                "Incorrectly made request.",
                message.toString(),
                LocalDateTime.now()
        );
    }
}
