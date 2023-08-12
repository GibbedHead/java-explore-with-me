package ru.practicum.explorewithme.ewmservice.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.ewmservice.request.model.Request;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseUpdateRequestStatusDto {
    List<Request> confirmedRequests;
    List<Request> rejectedRequests;
}
