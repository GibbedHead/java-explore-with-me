package ru.practicum.explorewithme.ewmservice.compilation.mapper;

import org.mapstruct.*;
import ru.practicum.explorewithme.ewmservice.compilation.dto.RequestAddCompilationDto;
import ru.practicum.explorewithme.ewmservice.compilation.dto.RequestUpdateCompilationDto;
import ru.practicum.explorewithme.ewmservice.compilation.dto.ResponseCompilationDto;
import ru.practicum.explorewithme.ewmservice.compilation.model.Compilation;

@Mapper(componentModel = "spring")
public interface CompilationMapper {
    @Mapping(target = "id", ignore = true)
    Compilation addDtoToCompilation(RequestAddCompilationDto addCompilationDto);

    ResponseCompilationDto compilationToResponseDto(Compilation compilation);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCompilationFromRequestUpdateDto(RequestUpdateCompilationDto dto, @MappingTarget Compilation compilation);
}
