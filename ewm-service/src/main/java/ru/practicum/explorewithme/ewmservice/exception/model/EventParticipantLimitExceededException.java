package ru.practicum.explorewithme.ewmservice.exception.model;

public class EventParticipantLimitExceededException extends RuntimeException {
    public EventParticipantLimitExceededException(String message) {
        super(message);
    }
}
