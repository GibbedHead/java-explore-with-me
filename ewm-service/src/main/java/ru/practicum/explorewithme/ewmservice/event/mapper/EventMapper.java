package ru.practicum.explorewithme.ewmservice.event.mapper;

import org.mapstruct.*;
import ru.practicum.explorewithme.ewmservice.category.model.Category;
import ru.practicum.explorewithme.ewmservice.event.dto.RequestAddEventDto;
import ru.practicum.explorewithme.ewmservice.event.dto.RequestUpdateEventDto;
import ru.practicum.explorewithme.ewmservice.event.dto.ResponseFullEventDto;
import ru.practicum.explorewithme.ewmservice.event.dto.ResponseShortEventDto;
import ru.practicum.explorewithme.ewmservice.event.model.Event;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "state", ignore = true)
    Event addDtoToEvent(RequestAddEventDto addEventDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    Category mapCategoryIdToCategory(Long category);

    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "views", ignore = true)
    ResponseFullEventDto eventToResponseFullDto(Event event);

    @Mapping(target = "confirmedRequests", ignore = true)
    @Mapping(target = "views", ignore = true)
    ResponseShortEventDto eventToResponseShortDto(Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "publishedOn", ignore = true)
    @Mapping(target = "initiator", ignore = true)
    @Mapping(target = "state", ignore = true)
    void updateEventFromRequestUpdateDto(RequestUpdateEventDto updateEventDto, @MappingTarget Event event);
}
