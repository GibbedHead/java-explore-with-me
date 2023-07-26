package ru.practicum.explorewithme.user.service;

import ru.practicum.explorewithme.user.dto.RequestAddUserDto;
import ru.practicum.explorewithme.user.dto.ResponseUserDto;

public interface UserService {
    ResponseUserDto addUser(RequestAddUserDto addUserDto);
}
