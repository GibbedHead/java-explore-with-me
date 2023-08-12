package ru.practicum.explorewithme.ewmservice.exception.model;

public class EntityHaveDependants extends RuntimeException {
    public EntityHaveDependants(String message) {
        super(message);
    }
}
