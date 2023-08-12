package ru.practicum.explorewithme.ewmservice.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.compilation.dto.RequestAddCompilationDto;
import ru.practicum.explorewithme.ewmservice.compilation.dto.RequestUpdateCompilationDto;
import ru.practicum.explorewithme.ewmservice.compilation.dto.ResponseCompilationDto;
import ru.practicum.explorewithme.ewmservice.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseCompilationDto add(
            @Valid @RequestBody RequestAddCompilationDto addCompilationDto
    ) {
        log.info("Add compilation request: {}", addCompilationDto);
        return compilationService.add(addCompilationDto);
    }

    @PatchMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    ResponseCompilationDto update(
            @Positive
            @PathVariable Long compId,
            @Valid @RequestBody RequestUpdateCompilationDto updateCompilationDto
    ) {
        log.info("Update compilation id={} request: {}", compId, updateCompilationDto);
        return compilationService.update(compId, updateCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(
            @Positive
            @PathVariable Long compId
    ) {
        log.info("Delete compilation id={} request", compId);
        compilationService.deleteById(compId);
    }
}
