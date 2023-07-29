package ru.practicum.explorewithme.ewmservice.category.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseCategoryDto {
    Long id;
    String name;
}
