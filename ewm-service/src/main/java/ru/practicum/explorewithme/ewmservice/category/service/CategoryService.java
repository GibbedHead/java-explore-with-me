package ru.practicum.explorewithme.ewmservice.category.service;

import ru.practicum.explorewithme.ewmservice.category.dto.RequestAddCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.dto.ResponseCategoryDto;

public interface CategoryService {
    ResponseCategoryDto addCategory(RequestAddCategoryDto addCategoryDto);
}
