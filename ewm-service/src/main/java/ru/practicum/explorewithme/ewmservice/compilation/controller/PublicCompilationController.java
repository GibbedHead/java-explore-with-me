package ru.practicum.explorewithme.ewmservice.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.compilation.dto.ResponseCompilationDto;
import ru.practicum.explorewithme.ewmservice.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Collection<ResponseCompilationDto> findAllPaged(
            @RequestParam(required = false) Boolean pinned,
            @PositiveOrZero(message = "From parameter must be positive or zero")
            @RequestParam(defaultValue = "0") Integer from,
            @Positive(message = "Size parameter must be positive")
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Find all compilation request by:\n\tpinned={}\n\tfrom={}\n\tsize={}", pinned, from, size);
        return compilationService.findAllPaged(pinned, from, size);
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseCompilationDto findById(
            @Positive
            @PathVariable Long compId
    ) {
        log.info("Find compilation request by id = {}", compId);
        return compilationService.findById(compId);
    }
}
