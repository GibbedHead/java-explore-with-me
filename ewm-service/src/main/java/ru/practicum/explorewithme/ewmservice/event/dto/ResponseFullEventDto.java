package ru.practicum.explorewithme.ewmservice.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.ewmservice.category.model.Category;
import ru.practicum.explorewithme.ewmservice.event.model.Location;
import ru.practicum.explorewithme.ewmservice.event.state.EventState;
import ru.practicum.explorewithme.ewmservice.user.dto.ResponseUserShortDto;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseFullEventDto {
    Long id;
    String annotation;
    String description;
    String title;
    Category category;
    Integer participantLimit;
    Long confirmedRequests;
    LocalDateTime createdOn;
    LocalDateTime publishedOn;
    LocalDateTime eventDate;
    ResponseUserShortDto initiator;
    Location location;
    Boolean paid;
    Boolean requestModeration;
    EventState state;
    Long views;
}
