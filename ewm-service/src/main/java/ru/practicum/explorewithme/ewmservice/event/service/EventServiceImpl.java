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
import ru.practicum.explorewithme.ewmservice.event.mapper.ModerationCommentMapper;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.model.ModerationComment;
import ru.practicum.explorewithme.ewmservice.event.repository.EventRepository;
import ru.practicum.explorewithme.ewmservice.event.repository.ModerationCommentRepository;
import ru.practicum.explorewithme.ewmservice.event.sort.EventSortField;
import ru.practicum.explorewithme.ewmservice.event.state.EventModerationStateChangeAction;
import ru.practicum.explorewithme.ewmservice.event.state.EventModerationStateChangeAdminAction;
import ru.practicum.explorewithme.ewmservice.event.state.EventState;
import ru.practicum.explorewithme.ewmservice.event.validator.EventValidator;
import ru.practicum.explorewithme.ewmservice.exception.model.EntityNotFoundException;
import ru.practicum.explorewithme.ewmservice.exception.model.EntityStateConflictException;
import ru.practicum.explorewithme.ewmservice.exception.model.ForbiddenAccessTypeException;
import ru.practicum.explorewithme.ewmservice.exception.model.WrongParameterIdsListException;
import ru.practicum.explorewithme.ewmservice.request.service.RequestService;
import ru.practicum.explorewithme.ewmservice.user.model.User;
import ru.practicum.explorewithme.ewmservice.user.repository.UserRepository;
import ru.practicum.explorewithme.statsclient.StatsClient;
import ru.practicum.explorewithme.statsdto.dto.AddHitDto;
import ru.practicum.explorewithme.statsdto.dto.ResponseStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
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
    private final ModerationCommentRepository moderationCommentRepository;
    private final RequestService requestService;
    private final StatsClient statsClient;
    private final EventMapper eventMapper = Mappers.getMapper(EventMapper.class);
    private final ModerationCommentMapper moderationCommentMapper = Mappers.getMapper(ModerationCommentMapper.class);

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
        ResponseFullEventDto fullEventDto = eventMapper.eventToResponseFullDto(foundEvent);
        addToFullEventDtoRequestsAndViews(fullEventDto);
        log.info("Event found {}", fullEventDto);
        return fullEventDto;
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
        ResponseFullEventDto fullEventDto = eventMapper.eventToResponseFullDto(updatedEvent);
        addToFullEventDtoRequestsAndViews(fullEventDto);
        log.info("Event updated {}", fullEventDto);
        return fullEventDto;
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
                (
                        updateEventAdminDto.getStateAction() == EventModerationStateChangeAdminAction.REJECT_EVENT
                                ||
                                updateEventAdminDto.getStateAction() == EventModerationStateChangeAdminAction.REQUIRE_EDIT
                )
                        &&
                        foundEvent.getState() == EventState.PUBLISHED
        ) {
            throw new EntityStateConflictException("Can't reject or return to edit published event.");
        }
        eventMapper.updateEventFromAdminRequestUpdateDto(updateEventAdminDto, foundEvent);
        adminUpdateEventState(updateEventAdminDto.getStateAction(), foundEvent);
        Event updatedEvent = eventRepository.save(foundEvent);
        ResponseFullEventDto fullEventDto = eventMapper.eventToResponseFullDto(updatedEvent);
        addToFullEventDtoRequestsAndViews(fullEventDto);
        log.info("Event updated {}", fullEventDto);
        return fullEventDto;

    }

    private void adminUpdateEventState(EventModerationStateChangeAdminAction action, Event event) {
        if (action != null) {
            switch (action) {
                case REQUIRE_EDIT:
                    event.setState(EventState.EDIT_REQUIRED);
                    break;
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    deleteModerationCommentsByEvent(event);
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
    public Collection<ResponseFullEventDto> findAdminByCriteria(List<Long> users,
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
        fullEventDtos.forEach(this::addToFullEventDtoRequestsAndViews);
        log.info("Found {} events", fullEventDtos.size());
        return fullEventDtos;
    }

    @Override
    public ResponseFullEventDto findPublicByEventId(Long eventId, HttpServletRequest request) {
        Event foundEvent = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        EVENT_NOT_FOUND_MESSAGE,
                        eventId
                ))
        );
        if (!foundEvent.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenAccessTypeException(String.format(
                    EVENT_NOT_FOUND_MESSAGE,
                    eventId
            ));
        }
        ResponseFullEventDto fullEventDto = eventMapper.eventToResponseFullDto(foundEvent);
        addToFullEventDtoRequestsAndViews(fullEventDto);
        log.info("Event found {}", fullEventDto);
        AddHitDto hit = new AddHitDto(
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        );
        statsClient.addHit(hit);
        return fullEventDto;
    }

    @Override
    public Collection<ResponseShortEventDto> findPublicByCriteria(
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
    ) {
        if (categories != null && categories.stream().reduce(0L, Long::sum) < categories.size()) {
            throw new WrongParameterIdsListException("Wrong category ids");
        }
        Sort sorting;
        if (sort == null) {
            sorting = Sort.by("eventDate").descending();
        } else {
            switch (sort) {
                case EVENT_DATE:
                    sorting = Sort.by("eventDate").descending();
                    break;
                case VIEWS:
                    sorting = Sort.by("id").descending();
                    break;
                default:
                    sorting = Sort.by("id").descending();
            }
        }
        Pageable pageable = PageRequest.of(from / size, size, sorting);
        List<ResponseShortEventDto> shortEventDtos = eventRepository.findAll(
                        byAnnotationAndDescriptionIgnoreCases(text)
                                .and(byCategoryIn(categories))
                                .and(byPaid(paid))
                                .and(byRangeStart(rangeStart))
                                .and(byRangeEnd(rangeEnd))
                                .and(byAvailability(onlyAvailable))
                                .and(onlyPublished()),
                        pageable
                ).stream()
                .map(eventMapper::eventToResponseShortDto)
                .collect(Collectors.toList());
        shortEventDtos.forEach(this::addToShortEventDtoRequestsAndViews);
        if (sort != null && sort.equals(EventSortField.VIEWS)) {
            shortEventDtos.sort(Comparator.comparing(ResponseShortEventDto::getViews).reversed());
        }
        AddHitDto hit = new AddHitDto(
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        );
        statsClient.addHit(hit);
        log.info("Found {} events", shortEventDtos.size());
        return shortEventDtos;
    }

    @Override
    public void addToFullEventDtoRequestsAndViews(ResponseFullEventDto responseFullEventDto) {
        responseFullEventDto.setConfirmedRequests(requestService.getConfirmedRequestCount(responseFullEventDto.getId()));
        Collection<ResponseStatsDto> statsClientStats = statsClient.getStats(
                LocalDateTime.now().minusYears(100),
                LocalDateTime.now().plusYears(100),
                List.of(String.format("/events/%d", responseFullEventDto.getId())),
                true
        );
        Long views;
        if (!statsClientStats.isEmpty()) {
            views = statsClientStats.iterator().next().getHits();
        } else {
            views = 0L;
        }
        responseFullEventDto.setViews(
                views
        );
    }

    @Override
    public void addToShortEventDtoRequestsAndViews(ResponseShortEventDto responseShortEventDto) {
        responseShortEventDto.setConfirmedRequests(requestService.getConfirmedRequestCount(responseShortEventDto.getId()));
        Collection<ResponseStatsDto> statsClientStats = statsClient.getStats(
                LocalDateTime.now().minusYears(100),
                LocalDateTime.now().plusYears(100),
                List.of(String.format("/events/%d", responseShortEventDto.getId())),
                true
        );
        Long views;
        if (!statsClientStats.isEmpty()) {
            views = statsClientStats.iterator().next().getHits();
        } else {
            views = 0L;
        }
        responseShortEventDto.setViews(
                views
        );
    }

    @Override
    public Set<ResponseShortEventDto> findShortDtoByIds(List<Long> ids) {
        Set<ResponseShortEventDto> shortEventDtos = eventRepository.findAll(byIdsIn(ids)).stream()
                .map(eventMapper::eventToResponseShortDto)
                .collect(Collectors.toSet());
        shortEventDtos.forEach(this::addToShortEventDtoRequestsAndViews);
        return shortEventDtos;
    }

    @Override
    public Set<Event> findByIds(List<Long> ids) {
        return new HashSet<>(eventRepository.findAll(byIdsIn(ids)));
    }

    @Override
    public ResponseShortModerationCommentDto addModerationComment(
            Long eventId, RequestAddModerationCommentDto addModerationCommentDto
    ) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        EVENT_NOT_FOUND_MESSAGE,
                        eventId
                ))
        );
        ModerationComment moderationComment = moderationCommentMapper.addDtoToModerationComment(addModerationCommentDto);
        moderationComment.setEvent(event);
        moderationComment.setCreatedAt(LocalDateTime.now());
        ModerationComment savedModerationComment = moderationCommentRepository.save(moderationComment);
        log.info("Moderation comment saved: {}", savedModerationComment);
        return moderationCommentMapper.moderationCommentToResponseShortDto(savedModerationComment);
    }

    @Override
    public Collection<ResponseFullEventDto> findPendingEventsPaged(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("eventDate").ascending());
        List<ResponseFullEventDto> pendingEvents = eventRepository.findByState(EventState.PENDING, pageable).stream()
                .map(eventMapper::eventToResponseFullDto)
                .collect(Collectors.toList());
        log.info("Found {} events", pendingEvents.size());
        return pendingEvents;
    }

    @Override
    public Collection<ResponseShortModerationCommentDto> findEventModerationComments(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        EVENT_NOT_FOUND_MESSAGE,
                        eventId
                ))
        );
        List<ModerationComment> moderationComment = moderationCommentRepository.findByEvent(event);
        log.info("Found {} moderation comments for event id = {}", moderationComment.size(), eventId);
        return moderationComment.stream()
                .map(moderationCommentMapper::moderationCommentToResponseShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteModerationCommentsByEvent(Event event) {
        Long deleteCommentsCount = moderationCommentRepository.deleteByEvent(event);
        log.info("Deleted {} moderation comments for event id = {}", deleteCommentsCount, event.getId());
    }
}
