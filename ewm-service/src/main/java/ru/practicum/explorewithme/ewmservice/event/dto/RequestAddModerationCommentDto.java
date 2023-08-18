package ru.practicum.explorewithme.ewmservice.event.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestAddModerationCommentDto {
    @NotBlank
    String comment;
}
