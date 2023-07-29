package ru.practicum.explorewithme.ewmservice.category.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.ewmservice.category.dto.RequestAddCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.dto.ResponseCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category addDtoToCategory(RequestAddCategoryDto addCategoryDto);

    ResponseCategoryDto categoryToResponseDto(Category category);
}
