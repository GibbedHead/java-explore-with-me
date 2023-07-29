package ru.practicum.explorewithme.ewmservice.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.ewmservice.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
