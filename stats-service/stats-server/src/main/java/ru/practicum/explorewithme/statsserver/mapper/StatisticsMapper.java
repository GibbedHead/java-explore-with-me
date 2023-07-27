package ru.practicum.explorewithme.statsserver.mapper;

import org.mapstruct.Mapper;
import ru.practicum.explorewithme.statsdto.AddHitDto;
import ru.practicum.explorewithme.statsserver.model.Statistics;

@Mapper(componentModel = "spring")
public interface StatisticsMapper {
    Statistics addHitToStatistics(AddHitDto addHitDto);
}
