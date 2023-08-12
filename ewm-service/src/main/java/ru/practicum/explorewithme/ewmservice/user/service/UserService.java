package ru.practicum.explorewithme.ewmservice.user.service;

import ru.practicum.explorewithme.ewmservice.user.dto.RequestAddUserDto;
import ru.practicum.explorewithme.ewmservice.user.dto.ResponseUserDto;

import java.util.Collection;
import java.util.List;

public interface UserService {
    ResponseUserDto addUser(RequestAddUserDto addUserDto);

    Collection<ResponseUserDto> findUsers(List<Long> ids, Integer from, Integer size);

    void deleteUser(Long id);
}
