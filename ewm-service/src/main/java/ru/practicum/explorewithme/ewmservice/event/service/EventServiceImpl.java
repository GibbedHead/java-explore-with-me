package ru.practicum.explorewithme.ewmservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.ewmservice.category.model.Category;
import ru.practicum.explorewithme.ewmservice.category.repository.CategoryRepository;
import ru.practicum.explorewithme.ewmservice.event.dto.*;
import ru.practicum.explorewithme.ewmservice.event.mapper.EventMapper;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.repository.EventRepository;
import ru.practicum.explorewithme.ewmservice.event.state.EventModerationStateChangeAction;
import ru.practicum.explorewithme.ewmservice.event.state.EventModerationStateChangeAdminAction;
import ru.practicum.explorewithme.ewmservice.event.state.EventState;
import ru.practicum.explorewithme.ewmservice.event.validator.EventValidator;
import ru.practicum.explorewithme.ewmservice.exception.model.EntityNotFoundException;
import ru.practicum.explorewithme.ewmservice.exception.model.EntityStateConflictException;
import ru.practicum.explorewithme.ewmservice.exception.model.ForbiddenAccessTypeException;
import ru.practicum.explorewithme.ewmservice.request.service.RequestService;
import ru.practicum.explorewithme.ewmservice.user.model.User;
import ru.practicum.explorewithme.ewmservice.user.repository.UserRepository;
import ru.practicum.explorewithme.statsclient.StatsClient;
import ru.practicum.explorewithme.statsdto.dto.AddHitDto;
import ru.practicum.explorewithme.statsdto.dto.ResponseStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.ewmservice.event.repository.EventRepository.Specifications.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    public static final String EVENT_NOT_FOUND_MESSAGE = "Event id#%d not found";
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestService requestService;
    private final StatsClient statsClient;
    private final EventMapper eventMapper = Mappers.getMapper(EventMapper.class);

    @Override
    public ResponseFullEventDto addEvent(Long userId, RequestAddEventDto addEventDto) {
        User initiator = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        "User id#%d not found",
                        userId
                ))
        );
        Long categoryId = addEventDto.getCategory();
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        "Category id#%d not found",
                        categoryId
                ))
        );
        Event event = eventMapper.addDtoToEvent(addEventDto);
        event.setInitiator(initiator);
        event.setCategory(category);
        event.setState(EventState.PENDING);
        Event savedEvent = eventRepository.save(event);
        log.info("Event saved: {}", savedEvent);
        return eventMapper.eventToResponseFullDto(savedEvent);
    }

    @Override
    public Collection<ResponseShortEventDto> findByUserIdPaged(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> userEvents = eventRepository.findByInitiatorId(userId, pageable);
        log.info("Found {} user id = {}", userEvents.size(), userEvents);
        return userEvents.stream()
                .map(eventMapper::eventToResponseShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseFullEventDto findUserEventByEventId(Long userId, Long eventId) {
        Event foundEvent = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        EVENT_NOT_FOUND_MESSAGE,
                        eventId
                ))
        );
        if (!Objects.equals(userId, foundEvent.getInitiator().getId())) {
            throw new ForbiddenAccessTypeException(String.format("Access to event %d forbidden", eventId));
        }
        log.info("Found event: {}", foundEvent);
        return eventMapper.eventToResponseFullDto(foundEvent);
    }

    @Override
    public ResponseFullEventDto updateEvent(Long userId, Long eventId, RequestUpdateEventDto updateEventDto) {
        Event foundEvent = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        EVENT_NOT_FOUND_MESSAGE,
                        eventId
                ))
        );
        if (!Objects.equals(userId, foundEvent.getInitiator().getId())) {
            throw new ForbiddenAccessTypeException(String.format("Access to event %d forbidden", eventId));
        }
        if (!EventValidator.isValidStateForUpdate(foundEvent)) {
            throw new EntityStateConflictException(
                    String.format(
                            "Event state %s don't allow updates",
                            foundEvent.getState()
                    )
            );
        }
        eventMapper.updateEventFromRequestUpdateDto(updateEventDto, foundEvent);
        updateEventState(updateEventDto.getStateAction(), foundEvent);
        Event updatedEvent = eventRepository.save(foundEvent);
        log.info("Event updated: {}", updatedEvent);
        return eventMapper.eventToResponseFullDto(updatedEvent);
    }

    private void updateEventState(EventModerationStateChangeAction action, Event event) {
        if (action != null) {
            switch (action) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public ResponseFullEventDto adminUpdateEvent(Long eventId, RequestUpdateEventAdminDto updateEventAdminDto) {
        Event foundEvent = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        EVENT_NOT_FOUND_MESSAGE,
                        eventId
                ))
        );
        if (
                updateEventAdminDto.getStateAction() == EventModerationStateChangeAdminAction.PUBLISH_EVENT
                        &&
                        foundEvent.getState() != EventState.PENDING
        ) {
            throw new EntityStateConflictException("Only pending events can be published.");
        }
        if (
                updateEventAdminDto.getStateAction() == EventModerationStateChangeAdminAction.REJECT_EVENT
                        &&
                        foundEvent.getState() == EventState.PUBLISHED
        ) {
            throw new EntityStateConflictException("Can't reject published event.");
        }
        eventMapper.updateEventFromAdminRequestUpdateDto(updateEventAdminDto, foundEvent);
        adminUpdateEventState(updateEventAdminDto.getStateAction(), foundEvent);
        Event updatedEvent = eventRepository.save(foundEvent);
        log.info("Event updated: {}", updatedEvent);
        return eventMapper.eventToResponseFullDto(updatedEvent);
    }

    private void adminUpdateEventState(EventModerationStateChangeAdminAction action, Event event) {
        if (action != null) {
            switch (action) {
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    break;
                case REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public Collection<ResponseFullEventDto> findEventsByCriteria(List<Long> users,
                                                                 List<EventState> states,
                                                                 List<Long> categories,
                                                                 LocalDateTime rangeStart,
                                                                 LocalDateTime rangeEnd,
                                                                 Integer from,
                                                                 Integer size) {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(from / size, size, sort);
        List<ResponseFullEventDto> fullEventDtos = eventRepository.findAll(
                        byUserIn(users)
                                .and(byStateIn(states))
                                .and(byCategoryIn(categories))
                                .and(byRangeStart(rangeStart))
                                .and(byRangeEnd(rangeEnd)),
                        pageable
                ).stream()
                .map(eventMapper::eventToResponseFullDto)
                .collect(Collectors.toList());
        fullEventDtos.forEach(e -> e.setConfirmedRequests(requestService.getConfirmedRequestCount(e.getId())));
        fullEventDtos.forEach(e -> {
                    Collection<ResponseStatsDto> statsClientStats = statsClient.getStats(
                            LocalDateTime.now().minusYears(100),
                            LocalDateTime.now().plusYears(100),
                            List.of(String.format("/events/%d", e.getId())),
                            false
                    );
                    Long views;
                    if (!statsClientStats.isEmpty()) {
                        views = statsClientStats.iterator().next().getHits();
                    } else {
                        views = 0L;
                    }
                    e.setViews(
                            views
                    );
                }
        );
        log.info("Found {} events", fullEventDtos.size());
        return fullEventDtos;
    }

    @Override
    public ResponseFullEventDto findPublicByEventId(Long id, HttpServletRequest request) {
        AddHitDto hit = new AddHitDto(
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        );
        statsClient.addHit(hit);
        return null;
    }

    @Override
    public ResponseFullEventDto findPublicByCriteria(HttpServletRequest request) {
        AddHitDto hit = new AddHitDto(
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        );
        statsClient.addHit(hit);
        return null;
    }
}
