package ru.practicum.explorewithme.ewmservice.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.category.dto.ResponseCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Collection<ResponseCategoryDto> findAllPaged(
            @PositiveOrZero(message = "From parameter must be positive or zero")
            @RequestParam(defaultValue = "0") Integer from,
            @Positive(message = "Size parameter must be positive")
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Find paged categories request. From: {}, size: {}", from, size);
        return categoryService.findAllPaged(from, size);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseCategoryDto findById(@PathVariable Long id) {
        log.info("Find categories id#{} request.", id);
        return categoryService.findById(id);
    }

}
