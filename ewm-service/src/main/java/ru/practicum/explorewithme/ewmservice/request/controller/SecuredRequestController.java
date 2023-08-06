package ru.practicum.explorewithme.ewmservice.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.request.dto.ResponseRequestDto;
import ru.practicum.explorewithme.ewmservice.request.service.RequestService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class SecuredRequestController {
    private final RequestService requestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Collection<ResponseRequestDto> findByUserId(
            @PathVariable Long userId
    ) {
        log.info("Find requests by user id={} request.", userId);
        return requestService.findByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseRequestDto add(
            @PathVariable Long userId,
            @RequestParam Long eventId
    ) {
        log.info("Add requests participation by user id={} for even id = {} request.", userId, eventId);
        return requestService.add(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    ResponseRequestDto cancelRequest(
            @PathVariable Long requestId
    ) {
        log.info("Cancel request id={} .", requestId);
        return requestService.cancelRequest(requestId);
    }
}
