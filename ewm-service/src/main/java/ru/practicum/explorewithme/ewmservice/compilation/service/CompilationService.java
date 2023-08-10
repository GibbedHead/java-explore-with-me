package ru.practicum.explorewithme.ewmservice.compilation.service;

import ru.practicum.explorewithme.ewmservice.compilation.dto.RequestAddCompilationDto;
import ru.practicum.explorewithme.ewmservice.compilation.dto.ResponseCompilationDto;

import java.util.Collection;

public interface CompilationService {
    ResponseCompilationDto add(RequestAddCompilationDto addCompilationDto);

    Collection<ResponseCompilationDto> findAllPaged(Boolean pinned, Integer from, Integer size);
}
