package ru.practicum.explorewithme.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.user.dto.RequestAddUserDto;
import ru.practicum.explorewithme.user.dto.ResponseUserDto;
import ru.practicum.explorewithme.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User addDtoToUser(RequestAddUserDto addUserDto);

    ResponseUserDto userToResponseDto(User user);
}
