package ru.practicum.explorewithme.ewmservice.user.service;

import ru.practicum.explorewithme.ewmservice.user.dto.RequestAddUserDto;
import ru.practicum.explorewithme.ewmservice.user.dto.ResponseUserDto;

public interface UserService {
    ResponseUserDto addUser(RequestAddUserDto addUserDto);
}
