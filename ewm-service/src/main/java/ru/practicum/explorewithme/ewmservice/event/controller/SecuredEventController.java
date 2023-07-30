package ru.practicum.explorewithme.ewmservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.event.dto.RequestAddEventDto;
import ru.practicum.explorewithme.ewmservice.event.dto.RequestUpdateEventDto;
import ru.practicum.explorewithme.ewmservice.event.dto.ResponseFullEventDto;
import ru.practicum.explorewithme.ewmservice.event.dto.ResponseShortEventDto;
import ru.practicum.explorewithme.ewmservice.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class SecuredEventController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseFullEventDto addEvent(
            @PathVariable Long userId,
            @Valid @RequestBody RequestAddEventDto addEventDto
    ) {
        log.info("Add event request from user id#{}: {}", userId, addEventDto);
        return eventService.addEvent(userId, addEventDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Collection<ResponseShortEventDto> findByUserIdPaged(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @Positive(message = "Size parameter must be positive")
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Find paged user id={} events request. From: {}, size: {}", userId, from, size);
        return eventService.findByUserIdPaged(userId, from, size);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseFullEventDto findUserEventByEventId(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        log.info("Find user id={} event by id={} request.", userId, eventId);
        return eventService.findUserEventByEventId(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseFullEventDto updateEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody RequestUpdateEventDto updateEventDto
    ) {
        log.info("Find user id={} event by id={} request.", userId, eventId);
        return eventService.findUserEventByEventId(userId, eventId);
    }
}
