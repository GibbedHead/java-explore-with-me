package ru.practicum.explorewithme.ewmservice.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.ewmservice.request.status.RequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseRequestDto {
    Long id;
    Long event;
    Long requester;
    LocalDateTime created;
    RequestStatus status;
}
