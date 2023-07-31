package ru.practicum.explorewithme.ewmservice.event.service;

import ru.practicum.explorewithme.ewmservice.event.dto.RequestAddEventDto;
import ru.practicum.explorewithme.ewmservice.event.dto.RequestUpdateEventDto;
import ru.practicum.explorewithme.ewmservice.event.dto.ResponseFullEventDto;
import ru.practicum.explorewithme.ewmservice.event.dto.ResponseShortEventDto;

import java.util.Collection;

public interface EventService {
    ResponseFullEventDto addEvent(Long userId, RequestAddEventDto addEventDto);

    Collection<ResponseShortEventDto> findByUserIdPaged(Long userId, Integer from, Integer size);

    ResponseFullEventDto findUserEventByEventId(Long userId, Long eventId);

    ResponseFullEventDto updateEvent(Long userId, Long eventId, RequestUpdateEventDto updateEventDto);
}
