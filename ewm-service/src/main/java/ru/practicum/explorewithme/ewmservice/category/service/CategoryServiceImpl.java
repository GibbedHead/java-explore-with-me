package ru.practicum.explorewithme.ewmservice.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.ewmservice.category.dto.RequestAddCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.dto.RequestUpdateCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.dto.ResponseCategoryDto;
import ru.practicum.explorewithme.ewmservice.category.mapper.CategoryMapper;
import ru.practicum.explorewithme.ewmservice.category.model.Category;
import ru.practicum.explorewithme.ewmservice.category.repository.CategoryRepository;
import ru.practicum.explorewithme.ewmservice.category.validator.CategoryValidator;
import ru.practicum.explorewithme.ewmservice.exception.model.EntityHaveDependants;
import ru.practicum.explorewithme.ewmservice.exception.model.EntityNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;
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

    @Override
    public void deleteById(Long id) {
        if (categoryValidator.isCategoryHaveEvents(id)) {
            throw new EntityHaveDependants(
                    String.format("Category id#%d have linked events", id)
            );
        }
        try {
            categoryRepository.deleteById(id);
            log.info("Category id#{} deleted", id);
        } catch (EmptyResultDataAccessException e) {
            String message = String.format("Category id#%d not found", id);
            log.error(message);
            throw new EntityNotFoundException(message);
        }
    }

    @Override
    public Collection<ResponseCategoryDto> findAllPaged(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Category> foundCategories = categoryRepository.findAll(pageable).getContent();
        log.info("Found {} categories", foundCategories.size());
        return foundCategories.stream()
                .map(categoryMapper::categoryToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseCategoryDto findById(Long id) {
        Category foundCategory = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        "Category id#%d not found",
                        id
                ))
        );
        log.info("Category id#{} found: {}", id, foundCategory);
        return categoryMapper.categoryToResponseDto(foundCategory);
    }
}
