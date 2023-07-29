package ru.practicum.explorewithme.ewmservice.exception.model;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
