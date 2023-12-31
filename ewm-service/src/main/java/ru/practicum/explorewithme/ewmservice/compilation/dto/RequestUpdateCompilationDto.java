package ru.practicum.explorewithme.ewmservice.compilation.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.explorewithme.ewmservice.event.model.Event;

import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestUpdateCompilationDto {
    Set<Event> events;
    @JsonSetter(nulls = Nulls.SKIP)
    Boolean pinned = false;
    @Size(min = 1, max = 50, message = "Title length must be between 1 and 50")
    String title;
}
