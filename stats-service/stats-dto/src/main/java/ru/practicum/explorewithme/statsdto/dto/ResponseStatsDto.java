package ru.practicum.explorewithme.statsdto.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseStatsDto {
    String app;
    String uri;
    Long hits;
}
