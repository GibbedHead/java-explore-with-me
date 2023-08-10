package ru.practicum.explorewithme.ewmservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.event.dto.ResponseFullEventDto;
import ru.practicum.explorewithme.ewmservice.event.dto.ResponseShortEventDto;
import ru.practicum.explorewithme.ewmservice.event.service.EventService;
import ru.practicum.explorewithme.ewmservice.event.sort.EventSortField;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Collection<ResponseShortEventDto> findPublicByCriteria(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) LocalDateTime rangeStart,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam(required = false) LocalDateTime rangeEnd,
            @RequestParam(required = false) Boolean onlyAvailable,
            @RequestParam(required = false) EventSortField sort,
            @PositiveOrZero(message = "From parameter must be positive or zero")
            @RequestParam(defaultValue = "0") Integer from,
            @Positive(message = "Size parameter must be positive")
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        log.info(
                "Show events request by:\n\ttext={}\n\tcategories={}\n\tpaid={}\n\trangeStart={}\n\t" +
                        "rangeEnd={}\n\tonlyAvailable={}\n\tsort={}\n\tfrom={}\n\tsize={}",
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size
        );
        return eventService.findPublicByCriteria(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size,
                request
        );
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
