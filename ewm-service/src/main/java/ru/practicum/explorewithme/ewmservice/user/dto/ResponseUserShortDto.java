package ru.practicum.explorewithme.ewmservice.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseUserShortDto {
    Long id;
    String name;
}
