package ru.practicum.explorewithme.ewmservice.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.ewmservice.request.dto.ResponseRequestDto;
import ru.practicum.explorewithme.ewmservice.request.mapper.RequestMapper;
import ru.practicum.explorewithme.ewmservice.request.repository.RequestRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper = Mappers.getMapper(RequestMapper.class);

    @Override
    public Collection<ResponseRequestDto> findByUserId(Long userId) {
        return requestRepository.findByRequester(userId).stream()
                .map(requestMapper::requestToResponseDto)
                .collect(Collectors.toList());
    }
}
