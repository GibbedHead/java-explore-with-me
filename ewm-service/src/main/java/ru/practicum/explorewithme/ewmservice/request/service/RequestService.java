package ru.practicum.explorewithme.ewmservice.request.service;

import ru.practicum.explorewithme.ewmservice.request.dto.RequestUpdateRequestStatusDto;
import ru.practicum.explorewithme.ewmservice.request.dto.ResponseRequestDto;
import ru.practicum.explorewithme.ewmservice.request.dto.ResponseUpdateRequestStatusDto;

import java.util.Collection;

public interface RequestService {
    Collection<ResponseRequestDto> findByUserId(Long userId);

    ResponseRequestDto add(Long userId, Long eventId);

    ResponseRequestDto cancelRequest(Long requestId);

    Collection<ResponseRequestDto> findByEventId(Long eventId);

    ResponseUpdateRequestStatusDto updateRequestsStatus(
            Long eventId,
            RequestUpdateRequestStatusDto updateRequestStatusDto
    );
}
