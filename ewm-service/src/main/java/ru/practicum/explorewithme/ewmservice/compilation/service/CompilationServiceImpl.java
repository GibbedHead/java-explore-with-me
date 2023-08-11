package ru.practicum.explorewithme.ewmservice.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.ewmservice.compilation.dto.RequestAddCompilationDto;
import ru.practicum.explorewithme.ewmservice.compilation.dto.RequestUpdateCompilationDto;
import ru.practicum.explorewithme.ewmservice.compilation.dto.ResponseCompilationDto;
import ru.practicum.explorewithme.ewmservice.compilation.mapper.CompilationMapper;
import ru.practicum.explorewithme.ewmservice.compilation.model.Compilation;
import ru.practicum.explorewithme.ewmservice.compilation.repository.CompilationRepository;
import ru.practicum.explorewithme.ewmservice.event.dto.ResponseShortEventDto;
import ru.practicum.explorewithme.ewmservice.event.model.Event;
import ru.practicum.explorewithme.ewmservice.event.service.EventService;
import ru.practicum.explorewithme.ewmservice.exception.model.EntityNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.ewmservice.compilation.repository.CompilationRepository.Specifications.byPinned;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    public static final String COMPILATION_NOT_FOUND_MESSAGE = "Compilation id#%d not found";
    private final EventService eventService;
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper = Mappers.getMapper(CompilationMapper.class);

    @Override
    public ResponseCompilationDto add(RequestAddCompilationDto addCompilationDto) {
        Compilation savedCompilation = compilationRepository.save(
                compilationMapper.addDtoToCompilation(addCompilationDto)
        );
        log.info("Compilation saved: {}", savedCompilation);
        ResponseCompilationDto responseCompilationDto = compilationMapper.compilationToResponseDto(savedCompilation);
        if (addCompilationDto.getEvents() != null && !addCompilationDto.getEvents().isEmpty()) {
            Set<ResponseShortEventDto> shortDtoByIds = eventService.findShortDtoByIds(
                    savedCompilation.getEvents().stream().map(Event::getId).collect(Collectors.toList())
            );
            responseCompilationDto.setEvents(shortDtoByIds);
        }
        return responseCompilationDto;
    }

    @Override
    public Collection<ResponseCompilationDto> findAllPaged(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> foundCompilations = compilationRepository.findAll(byPinned(pinned), pageable).toList();
        List<ResponseCompilationDto> responseCompilationDtos = foundCompilations.stream()
                .map(compilationMapper::compilationToResponseDto)
                .collect(Collectors.toList());
        responseCompilationDtos
                .forEach(
                        compilationDto -> compilationDto.getEvents()
                                .forEach(
                                        eventService::addToShortEventDtoRequestsAndViews
                                )
                );
        log.info("Found {} compilations", responseCompilationDtos.size());
        return responseCompilationDtos;
    }

    @Override
    public ResponseCompilationDto findById(Long compId) {
        Compilation foundCompilation = compilationRepository.findById(compId).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        COMPILATION_NOT_FOUND_MESSAGE,
                        compId
                ))
        );
        ResponseCompilationDto responseCompilationDto = compilationMapper.compilationToResponseDto(
                foundCompilation
        );
        responseCompilationDto.getEvents().forEach(eventService::addToShortEventDtoRequestsAndViews);
        log.info("Compilation found: {}", foundCompilation);
        return responseCompilationDto;
    }

    @Override
    public ResponseCompilationDto update(Long compId, RequestUpdateCompilationDto updateCompilationDto) {
        Compilation foundCompilation = compilationRepository.findById(compId).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                        COMPILATION_NOT_FOUND_MESSAGE,
                        compId
                ))
        );
        compilationMapper.updateCompilationFromRequestUpdateDto(updateCompilationDto, foundCompilation);
        if (updateCompilationDto.getEvents() != null && !updateCompilationDto.getEvents().isEmpty()) {
            Set<Event> updatedEvents = eventService.findByIds(
                    updateCompilationDto.getEvents().stream()
                            .map(Event::getId)
                            .collect(Collectors.toList())
            );
            foundCompilation.setEvents(updatedEvents);
        }
        Compilation updatedCompilation = compilationRepository.save(foundCompilation);
        log.info("Compilation updated: {}", updatedCompilation);
        ResponseCompilationDto responseCompilationDto = compilationMapper.compilationToResponseDto(updatedCompilation);
        responseCompilationDto.getEvents().forEach(eventService::addToShortEventDtoRequestsAndViews);
        return responseCompilationDto;
    }

    @Override
    public void deleteById(Long compId) {
        try {
            compilationRepository.deleteById(compId);
            log.info("Compilation id#{} deleted", compId);
        } catch (EmptyResultDataAccessException e) {
            String message = String.format(COMPILATION_NOT_FOUND_MESSAGE, compId);
            log.error(message);
            throw new EntityNotFoundException(message);
        }
    }
}
