package ru.practicum.explorewithme.ewmservice.category.mapper;

import org.mapstruct.*;
import ru.practicum.explorewithme.ewmservice.category.dto.RequestAddCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.dto.RequestUpdateCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.dto.ResponseCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    Category addDtoToCategory(RequestAddCategoryDto addCategoryDto);

    ResponseCategoryDto categoryToResponseDto(Category category);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCategoryFromRequestUpdateDto(RequestUpdateCategoryDto dto, @MappingTarget Category category);
}
