package ru.practicum.explorewithme.ewmservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.ewmservice.event.model.Location;
import ru.practicum.explorewithme.ewmservice.event.validator.constraint.AtLeastTwoHoursInFuture;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestAddEventDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = "Annotation length must be between 20 and 2000")
    String annotation;
    @NotBlank
    @Size(min = 3, max = 120, message = "Title length must be between 3 and 120")
    String title;
    @NotBlank
    @Size(min = 20, max = 7000, message = "Description length must be between 20 and 7000")
    String description;
    @NotNull
    @Positive
    Long category;
    @NotNull
    @AtLeastTwoHoursInFuture
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    @NotNull
    Location location;
    @JsonSetter(nulls = Nulls.SKIP)
    boolean paid = false;
    @JsonSetter(nulls = Nulls.SKIP)
    @PositiveOrZero
    Integer participantLimit = 0;
    @JsonSetter(nulls = Nulls.SKIP)
    Boolean requestModeration = true;

}
