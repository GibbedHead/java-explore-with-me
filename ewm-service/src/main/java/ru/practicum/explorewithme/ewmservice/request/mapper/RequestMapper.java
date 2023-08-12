package ru.practicum.explorewithme.ewmservice.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.ewmservice.request.dto.ResponseRequestDto;
import ru.practicum.explorewithme.ewmservice.request.model.Request;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    ResponseRequestDto requestToResponseDto(Request request);
}
