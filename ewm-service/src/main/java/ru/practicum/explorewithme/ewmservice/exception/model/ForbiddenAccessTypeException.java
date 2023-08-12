package ru.practicum.explorewithme.ewmservice.exception.model;

public class ForbiddenAccessTypeException extends RuntimeException {
    public ForbiddenAccessTypeException(String message) {
        super(message);
    }
}
