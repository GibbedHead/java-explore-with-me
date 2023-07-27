package ru.practicum.explorewithme.ewmservice.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.ewmservice.user.dto.RequestAddUserDto;
import ru.practicum.explorewithme.ewmservice.user.dto.ResponseUserDto;
import ru.practicum.explorewithme.ewmservice.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User addDtoToUser(RequestAddUserDto addUserDto);

    ResponseUserDto userToResponseDto(User user);
}
