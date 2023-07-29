package ru.practicum.explorewithme.ewmservice.category.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestUpdateCategoryDto {
    @NotBlank
    @Size(min = 1, message = "Name length must be between 1 and 50")
    @Size(max = 50, message = "Name length must be between 1 and 50")
    String name;
}
