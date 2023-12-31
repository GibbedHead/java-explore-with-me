package ru.practicum.explorewithme.ewmservice.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.repository.EventRepository;
import ru.practicum.explorewithme.ewmservice.event.state.EventState;
import ru.practicum.explorewithme.ewmservice.exception.model.*;
import ru.practicum.explorewithme.ewmservice.request.dto.RequestUpdateRequestStatusDto;
import ru.practicum.explorewithme.ewmservice.request.dto.ResponseRequestDto;
import ru.practicum.explorewithme.ewmservice.request.dto.ResponseUpdateRequestStatusDto;
import ru.practicum.explorewithme.ewmservice.request.mapper.RequestMapper;
import ru.practicum.explorewithme.ewmservice.request.model.Request;
import ru.practicum.explorewithme.ewmservice.request.repository.RequestRepository;
import ru.practicum.explorewithme.ewmservice.request.status.RequestStatus;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestMapper = Mappers.getMapper(RequestMapper.class);

    @Override
    public Collection<ResponseRequestDto> findByUserId(Long userId) {
        return requestRepository.findByRequester(userId).stream()
                .map(requestMapper::requestToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseRequestDto add(Long userId, Long eventId) {
        Event foundEvent = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        "Event id#%d not found",
                        eventId
                ))
        );
        if (userId.equals(foundEvent.getInitiator().getId())) {
            throw new OwnEventParticipationRequestException("Can't request own event participation");
        }
        if (!foundEvent.getState().equals(EventState.PUBLISHED)) {
            throw new EntityStateConflictException("Only published events allowed to get participation requests.");
        }
        Long confirmedRequests = getConfirmedRequestCount(eventId);
        if (
                foundEvent.getParticipantLimit() != 0
                        &&
                        confirmedRequests >= foundEvent.getParticipantLimit()
        ) {
            throw new EventParticipantLimitExceededException("Event participant limit exceeded.");
        }
        Request request = new Request();
        request.setEvent(eventId);
        request.setRequester(userId);
        request.setCreated(LocalDateTime.now());
        if (foundEvent.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            if (Boolean.TRUE.equals(foundEvent.getRequestModeration())) {
                request.setStatus(RequestStatus.PENDING);
            } else {
                request.setStatus(RequestStatus.CONFIRMED);
            }
        }
        try {
            Request savedRequest = requestRepository.save(request);
            log.info("Request saved: {}", savedRequest);
            return requestMapper.requestToResponseDto(savedRequest);
        } catch (ConstraintViolationException e) {
            throw new DBConstraintViolationException("Non unique participation request");
        }
    }

    @Override
    public ResponseRequestDto cancelRequest(Long requestId) {
        Request foundRequest = requestRepository.findById(requestId).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        "Request id#%d not found",
                        requestId
                ))
        );
        foundRequest.setStatus(RequestStatus.CANCELED);
        Request savedRequest = requestRepository.save(foundRequest);
        log.info("Request canceled: {}", savedRequest);
        return requestMapper.requestToResponseDto(savedRequest);
    }

    @Override
    public Collection<ResponseRequestDto> findByEventId(Long eventId) {
        return requestRepository.findByEvent(eventId).stream()
                .map(requestMapper::requestToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseUpdateRequestStatusDto updateRequestsStatus(
            Long eventId,
            RequestUpdateRequestStatusDto updateRequestStatusDto
    ) {
        if (
                updateRequestStatusDto.getRequestIds() == null
                        ||
                        updateRequestStatusDto.getRequestIds().isEmpty()
        ) {
            return new ResponseUpdateRequestStatusDto();
        }
        Event foundEvent = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        "Event id#%d not found",
                        eventId
                ))
        );
        if (
                foundEvent.getParticipantLimit() == 0
                        ||
                        Boolean.FALSE.equals(foundEvent.getRequestModeration())
        ) {
            return new ResponseUpdateRequestStatusDto();
        }
        Long confirmedRequestsCount = getConfirmedRequestCount(eventId);
        if (confirmedRequestsCount >= foundEvent.getParticipantLimit()) {
            throw new EventParticipantLimitExceededException("Event participant limit exceeded.");
        }
        List<Request> requests = requestRepository.findByIdIn(updateRequestStatusDto.getRequestIds());
        for (Request request : requests) {
            if (request.getStatus().equals(RequestStatus.PENDING)) {
                if (confirmedRequestsCount < foundEvent.getParticipantLimit()) {
                    confirmedRequestsCount++;
                    request.setStatus(updateRequestStatusDto.getStatus());
                } else {
                    request.setStatus(RequestStatus.REJECTED);
                }
            }
        }
        requests.forEach(requestRepository::save);
        List<Request> confirmedRequests = requests.stream()
                .filter(r -> r.getStatus().equals(RequestStatus.CONFIRMED))
                .collect(Collectors.toList());
        List<Request> rejectedRequests = requests.stream()
                .filter(r -> r.getStatus().equals(RequestStatus.REJECTED))
                .collect(Collectors.toList());
        return new ResponseUpdateRequestStatusDto(confirmedRequests, rejectedRequests);
    }

    @Override
    public Long getConfirmedRequestCount(Long eventId) {
        return requestRepository.countByEventAndStatus(eventId, RequestStatus.CONFIRMED);
    }
}
