package ru.practicum.explorewithme.statsserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.statsdto.dto.AddHitDto;
import ru.practicum.explorewithme.statsdto.dto.ResponseStatsDto;
import ru.practicum.explorewithme.statsserver.service.StatisticsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatisticsController {
    private final StatisticsService statisticsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(
            @Valid @RequestBody AddHitDto addHitDto
    ) {
        log.info("Add hit request: {}", addHitDto);
        statisticsService.addHit(addHitDto);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ResponseStatsDto> getStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") Boolean unique
    ) {
        log.info(
                "Get statistics request.\n\tFrom: {}\n\tTo: {}\n\tUris: {}\n\tUnique: {}",
                start,
                end,
                uris,
                unique
        );
        return statisticsService.getStats(start, end, uris, unique);
    }
}
