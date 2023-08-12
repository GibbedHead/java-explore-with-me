package ru.practicum.explorewithme.ewmservice.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.ewmservice.request.status.RequestStatus;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestUpdateRequestStatusDto {
    List<Long> requestIds;
    RequestStatus status;
}
