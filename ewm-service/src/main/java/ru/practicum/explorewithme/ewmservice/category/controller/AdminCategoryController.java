package ru.practicum.explorewithme.ewmservice.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.ewmservice.category.dto.RequestAddCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.dto.RequestUpdateCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.dto.ResponseCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseCategoryDto addCategory(@Valid @RequestBody RequestAddCategoryDto addCategoryDto) {
        log.info("Add category request: {}", addCategoryDto);
        return categoryService.addCategory(addCategoryDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseCategoryDto updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody RequestUpdateCategoryDto updateCategoryDto
    ) {
        log.info("Update category request: {}", updateCategoryDto);
        return categoryService.updateCategory(id, updateCategoryDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCategory(@PathVariable Long id) {
        log.info("Delete category id#{} request", id);
        categoryService.deleteById(id);
    }
}
