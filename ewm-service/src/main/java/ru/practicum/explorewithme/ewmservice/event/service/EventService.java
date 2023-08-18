package ru.practicum.explorewithme.ewmservice.event.service;

import ru.practicum.explorewithme.ewmservice.event.dto.*;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.sort.EventSortField;
import ru.practicum.explorewithme.ewmservice.event.state.EventState;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface EventService {
    ResponseFullEventDto addEvent(Long userId, RequestAddEventDto addEventDto);

    Collection<ResponseShortEventDto> findByUserIdPaged(Long userId, Integer from, Integer size);

    ResponseFullEventDto findUserEventByEventId(Long userId, Long eventId);

    ResponseFullEventDto updateEvent(Long userId, Long eventId, RequestUpdateEventDto updateEventDto);

    ResponseFullEventDto adminUpdateEvent(Long eventId, RequestUpdateEventAdminDto updateEventAdminDto);

    Collection<ResponseFullEventDto> findAdminByCriteria(List<Long> users,
                                                         List<EventState> states,
                                                         List<Long> categories,
                                                         LocalDateTime rangeStart,
                                                         LocalDateTime rangeEnd,
                                                         Integer from,
                                                         Integer size);

    ResponseFullEventDto findPublicByEventId(Long id, HttpServletRequest request);

    Collection<ResponseShortEventDto> findPublicByCriteria(
            String text,
            List<Long> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Boolean onlyAvailable,
            EventSortField sort,
            Integer from,
            Integer size,
            HttpServletRequest request
    );

    Set<ResponseShortEventDto> findShortDtoByIds(List<Long> ids);

    Set<Event> findByIds(List<Long> ids);

    void addToShortEventDtoRequestsAndViews(ResponseShortEventDto responseShortEventDto);

    void addToFullEventDtoRequestsAndViews(ResponseFullEventDto responseFullEventDto);

    ResponseShortModerationCommentDto addModerationComment(
            Long eventId, RequestAddModerationCommentDto addModerationCommentDto
    );
}
