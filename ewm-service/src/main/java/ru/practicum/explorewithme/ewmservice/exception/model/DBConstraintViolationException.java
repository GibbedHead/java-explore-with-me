package ru.practicum.explorewithme.ewmservice.exception.model;

public class DBConstraintViolationException extends RuntimeException {
    public DBConstraintViolationException(String message) {
        super(message);
    }
}
