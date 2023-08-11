package ru.practicum.explorewithme.ewmservice.compilation.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.ewmservice.event.dto.ResponseShortEventDto;

import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseCompilationDto {
    Long id;
    String title;
    Boolean pinned;
    Set<ResponseShortEventDto> events;
}
