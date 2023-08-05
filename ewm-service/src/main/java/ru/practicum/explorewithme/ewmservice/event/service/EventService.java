package ru.practicum.explorewithme.ewmservice.event.service;

import ru.practicum.explorewithme.ewmservice.event.dto.*;

import java.util.Collection;

public interface EventService {
    ResponseFullEventDto addEvent(Long userId, RequestAddEventDto addEventDto);

    Collection<ResponseShortEventDto> findByUserIdPaged(Long userId, Integer from, Integer size);

    ResponseFullEventDto findUserEventByEventId(Long userId, Long eventId);

    ResponseFullEventDto updateEvent(Long userId, Long eventId, RequestUpdateEventDto updateEventDto);

    ResponseFullEventDto adminUpdateEvent(Long eventId, RequestUpdateEventAdminDto updateEventAdminDto);
}
