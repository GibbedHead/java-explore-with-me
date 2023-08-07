package ru.practicum.explorewithme.ewmservice.event.service;

import ru.practicum.explorewithme.ewmservice.event.dto.*;
import ru.practicum.explorewithme.ewmservice.event.state.EventState;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface EventService {
    ResponseFullEventDto addEvent(Long userId, RequestAddEventDto addEventDto);

    Collection<ResponseShortEventDto> findByUserIdPaged(Long userId, Integer from, Integer size);

    ResponseFullEventDto findUserEventByEventId(Long userId, Long eventId);

    ResponseFullEventDto updateEvent(Long userId, Long eventId, RequestUpdateEventDto updateEventDto);

    ResponseFullEventDto adminUpdateEvent(Long eventId, RequestUpdateEventAdminDto updateEventAdminDto);

    Collection<ResponseFullEventDto> findEventsByCriteria(List<Long> users,
                                                          List<EventState> states,
                                                          List<Long> categories,
                                                          LocalDateTime rangeStart,
                                                          LocalDateTime rangeEnd,
                                                          Integer from,
                                                          Integer size);
}
