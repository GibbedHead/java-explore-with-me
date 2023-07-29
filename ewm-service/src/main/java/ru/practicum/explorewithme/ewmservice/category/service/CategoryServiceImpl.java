package ru.practicum.explorewithme.ewmservice.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.ewmservice.category.dto.RequestAddCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.dto.RequestUpdateCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.dto.ResponseCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.mapper.CategoryMapper;
import ru.practicum.explorewithme.ewmservice.category.model.Category;
import ru.practicum.explorewithme.ewmservice.category.repository.CategoryRepository;
import ru.practicum.explorewithme.ewmservice.exception.model.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper = Mappers.getMapper(CategoryMapper.class);

    @Override
    public ResponseCategoryDto addCategory(RequestAddCategoryDto addCategoryDto) {
        ResponseCategoryDto savedCategory = categoryMapper.categoryToResponseDto(
                categoryRepository.save(
                        categoryMapper.addDtoToCategory(addCategoryDto)
                )
        );
        log.info("Category saved: {}", savedCategory);
        return savedCategory;
    }

    @Override
    public ResponseCategoryDto updateCategory(Long id, RequestUpdateCategoryDto updateCategoryDto) {
        Category foundCategory = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        "Category id#%d not found",
                        id
                ))
        );
        categoryMapper.updateCategoryFromRequestUpdateDto(updateCategoryDto, foundCategory);
        Category updatedCategory = categoryRepository.save(foundCategory);
        log.info("Category updated: {}", updatedCategory);
        return categoryMapper.categoryToResponseDto(updatedCategory);
    }
}
