package ru.practicum.explorewithme.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseUserDto {
    Long id;
    String name;
    String email;
}
