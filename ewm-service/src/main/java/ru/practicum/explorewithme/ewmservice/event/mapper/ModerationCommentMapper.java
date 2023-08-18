package ru.practicum.explorewithme.ewmservice.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.explorewithme.ewmservice.event.dto.RequestAddModerationCommentDto;
import ru.practicum.explorewithme.ewmservice.event.dto.ResponseShortModerationCommentDto;
import ru.practicum.explorewithme.ewmservice.event.model.ModerationComment;

@Mapper(componentModel = "spring")
public interface ModerationCommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "event", ignore = true)
    ModerationComment addDtoToModerationComment(RequestAddModerationCommentDto addModerationCommentDto);

    ResponseShortModerationCommentDto moderationCommentToResponseShortDto(ModerationComment moderationComment);

}
