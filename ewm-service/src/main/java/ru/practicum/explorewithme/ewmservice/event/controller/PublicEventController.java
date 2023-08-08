package ru.practicum.explorewithme.ewmservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.event.dto.ResponseFullEventDto;
import ru.practicum.explorewithme.ewmservice.event.service.EventService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseFullEventDto findPublicByCriteria(
            HttpServletRequest request
    ) {
        log.info("Show event id={ request");
        return eventService.findPublicByCriteria(request);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseFullEventDto findPublicByEventId(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        log.info("Show event id={} request", id);
        return eventService.findPublicByEventId(id, request);
    }
}
