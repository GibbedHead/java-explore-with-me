package ru.practicum.explorewithme.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.dto.AddHitDto;
import ru.practicum.explorewithme.model.Statistics;

@Mapper(componentModel = "spring")
public interface StatisticsMapper {
    Statistics addHitToStatistics(AddHitDto addHitDto);
}
