package ru.practicum.explorewithme.ewmservice.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.ewmservice.category.dto.RequestAddCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.dto.ResponseCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.mapper.CategoryMapper;
import ru.practicum.explorewithme.ewmservice.category.repository.CategoryRepository;

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
}
