package ru.practicum.explorewithme.ewmservice.category.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.explorewithme.ewmservice.category.dto.RequestAddCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.dto.RequestUpdateCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.dto.ResponseCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.model.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category addDtoToCategory(RequestAddCategoryDto addCategoryDto);

    ResponseCategoryDto categoryToResponseDto(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCategoryFromRequestUpdateDto(RequestUpdateCategoryDto dto, @MappingTarget Category category);
}
