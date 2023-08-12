package ru.practicum.explorewithme.ewmservice.category.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.ewmservice.category.repository.CategoryRepository;

@Component
@RequiredArgsConstructor
public class CategoryValidator {
    private final CategoryRepository categoryRepository;

    public boolean isCategoryHaveEvents(Long id) {
        return false;
    }

}
