package ru.practicum.explorewithme.ewmservice.event.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseShortModerationCommentDto {
    Long id;
    String comment;
}
