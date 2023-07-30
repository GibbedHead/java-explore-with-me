package ru.practicum.explorewithme.ewmservice.event.service;

import ru.practicum.explorewithme.ewmservice.event.dto.RequestAddEventDto;
import ru.practicum.explorewithme.ewmservice.event.dto.ResponseFullEventDto;

public interface EventService {
    ResponseFullEventDto addEvent(Long userId, RequestAddEventDto addEventDto);
}
