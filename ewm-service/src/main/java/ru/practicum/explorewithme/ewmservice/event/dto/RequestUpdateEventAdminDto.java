package ru.practicum.explorewithme.ewmservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.ewmservice.event.model.Location;
import ru.practicum.explorewithme.ewmservice.event.state.EventModerationStateChangeAdminAction;
import ru.practicum.explorewithme.ewmservice.event.validator.constraint.AtLeastOneHourInFuture;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestUpdateEventAdminDto {
    @Size(min = 20, message = "Annotation length must be between 20 and 2000")
    @Size(max = 2000, message = "Annotation length must be between 20 and 2000")
    String annotation;
    @Size(min = 3, message = "Title length must be between 3 and 120")
    @Size(max = 120, message = "Title length must be between 3 and 120")
    String title;
    @Size(min = 20, message = "Description length must be between 20 and 7000")
    @Size(max = 7000, message = "Description length must be between 20 and 7000")
    String description;
    @Positive
    Long category;
    @AtLeastOneHourInFuture
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    Location location;
    Boolean paid;
    @PositiveOrZero
    Integer participantLimit;
    Boolean requestModeration;
    EventModerationStateChangeAdminAction stateAction;
}
