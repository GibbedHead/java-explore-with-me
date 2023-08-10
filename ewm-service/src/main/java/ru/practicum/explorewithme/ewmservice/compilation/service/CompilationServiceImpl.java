package ru.practicum.explorewithme.ewmservice.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.ewmservice.compilation.dto.RequestAddCompilationDto;
import ru.practicum.explorewithme.ewmservice.compilation.dto.ResponseCompilationDto;
import ru.practicum.explorewithme.ewmservice.compilation.mapper.CompilationMapper;
import ru.practicum.explorewithme.ewmservice.compilation.model.Compilation;
import ru.practicum.explorewithme.ewmservice.compilation.repository.CompilationRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explorewithme.ewmservice.compilation.repository.CompilationRepository.Specifications.byPinned;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper = Mappers.getMapper(CompilationMapper.class);

    @Override
    public ResponseCompilationDto add(RequestAddCompilationDto addCompilationDto) {
        Compilation savedCompilation = compilationRepository.save(
                compilationMapper.addDtoToCompilation(addCompilationDto)
        );
        log.info("Compilation saved: {}", savedCompilation);
        return compilationMapper.compilationToResponseDto(savedCompilation);
    }

    @Override
    public Collection<ResponseCompilationDto> findAllPaged(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> foundCompilations = compilationRepository.findAll(byPinned(pinned), pageable).toList();
        return foundCompilations.stream()
                .map(compilationMapper::compilationToResponseDto)
                .collect(Collectors.toList());
    }
}
