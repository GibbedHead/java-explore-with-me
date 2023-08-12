package ru.practicum.explorewithme.ewmservice.category.service;

import ru.practicum.explorewithme.ewmservice.category.dto.RequestAddCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.dto.RequestUpdateCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.dto.ResponseCategoryDto;

import java.util.Collection;

public interface CategoryService {
    ResponseCategoryDto addCategory(RequestAddCategoryDto addCategoryDto);

    ResponseCategoryDto updateCategory(Long id, RequestUpdateCategoryDto updateCategoryDto);

    void deleteById(Long id);

    Collection<ResponseCategoryDto> findAllPaged(Integer from, Integer size);

    ResponseCategoryDto findById(Long id);
}
