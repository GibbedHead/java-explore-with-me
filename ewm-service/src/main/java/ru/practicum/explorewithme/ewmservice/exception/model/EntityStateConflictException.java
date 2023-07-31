package ru.practicum.explorewithme.ewmservice.exception.model;

public class EntityStateConflictException extends RuntimeException {
    public EntityStateConflictException(String message) {
        super(message);
    }
}
