package ru.practicum.explorewithme.ewmservice.request.service;

import ru.practicum.explorewithme.ewmservice.request.dto.ResponseRequestDto;

import java.util.Collection;

public interface RequestService {
    Collection<ResponseRequestDto> findByUserId(Long userId);

    ResponseRequestDto add(Long userId, Long eventId);
}
