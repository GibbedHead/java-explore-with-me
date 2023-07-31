package ru.practicum.explorewithme.ewmservice.event.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.state.EventState;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventValidator {
    public static boolean isValidStateForUpdate(Event event) {
        List<EventState> validUpdateStates = List.of(EventState.PENDING, EventState.CANCELED);
        return validUpdateStates.contains(event.getState());
    }
}
