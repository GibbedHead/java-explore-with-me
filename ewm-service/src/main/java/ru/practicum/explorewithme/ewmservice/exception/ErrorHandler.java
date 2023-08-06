package ru.practicum.explorewithme.ewmservice.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.explorewithme.ewmservice.exception.model.*;

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
    public ExceptionResponseEntity handleConstraintViolationException(ConstraintViolationException ex) {
        return new ExceptionResponseEntity(
                HttpStatus.CONFLICT.toString(),
                "Integrity constraint has been violated.",
                ex.getCause().getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ExceptionResponseEntity handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ExceptionResponseEntity(
                HttpStatus.NOT_FOUND.toString(),
                "The required object was not found.",
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(EntityHaveDependants.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ExceptionResponseEntity handleEntityHaveDependants(EntityHaveDependants ex) {
        return new ExceptionResponseEntity(
                HttpStatus.CONFLICT.toString(),
                "For the requested operation the conditions are not met.",
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ExceptionResponseEntity handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return new ExceptionResponseEntity(
                HttpStatus.BAD_REQUEST.toString(),
                "Wrong parameter.",
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(ForbiddenAccessTypeException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ExceptionResponseEntity handleForbiddenAccessTypeException(ForbiddenAccessTypeException ex) {
        return new ExceptionResponseEntity(
                HttpStatus.NOT_FOUND.toString(),
                "Wrong access.",
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(EntityStateConflictException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ExceptionResponseEntity handleEntityStateConflictException(EntityStateConflictException ex) {
        return new ExceptionResponseEntity(
                HttpStatus.CONFLICT.toString(),
                "Wrong object state.",
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(OwnEventParticipationRequestException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ExceptionResponseEntity handleOwnEventParticipationRequestException(
            OwnEventParticipationRequestException ex
    ) {
        return new ExceptionResponseEntity(
                HttpStatus.CONFLICT.toString(),
                "Wrong operation type.",
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(EventParticipantLimitExceededException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ExceptionResponseEntity handleEventParticipantLimitExceededException(
            EventParticipantLimitExceededException ex
    ) {
        return new ExceptionResponseEntity(
                HttpStatus.CONFLICT.toString(),
                "Wrong event participation action.",
                ex.getMessage(),
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(DBConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ExceptionResponseEntity handleDBConstraintViolationException(
            DBConstraintViolationException ex
    ) {
        return new ExceptionResponseEntity(
                HttpStatus.CONFLICT.toString(),
                "Entity constraint violation.",
                ex.getMessage(),
                LocalDateTime.now()
        );
    }
}
