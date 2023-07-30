package ru.practicum.explorewithme.ewmservice.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.ewmservice.category.model.Category;
import ru.practicum.explorewithme.ewmservice.user.dto.ResponseUserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseShortEventDto {
    Long id;
    String annotation;
    String title;
    Category category;
    Integer confirmedRequests;
    LocalDateTime eventDate;
    ResponseUserShortDto initiator;
    Boolean paid;
    Long views;
}
