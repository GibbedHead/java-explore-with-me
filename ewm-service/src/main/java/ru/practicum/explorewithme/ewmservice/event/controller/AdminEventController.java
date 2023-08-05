package ru.practicum.explorewithme.ewmservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.event.dto.RequestUpdateEventAdminDto;
import ru.practicum.explorewithme.ewmservice.event.dto.ResponseFullEventDto;
import ru.practicum.explorewithme.ewmservice.event.service.EventService;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminEventController {
    private final EventService eventService;

    @PatchMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseFullEventDto update(
            @PathVariable Long eventId,
            @RequestBody RequestUpdateEventAdminDto updateEventAdminDto
    ) {
        return eventService.adminUpdateEvent(eventId, updateEventAdminDto);
    }
}
