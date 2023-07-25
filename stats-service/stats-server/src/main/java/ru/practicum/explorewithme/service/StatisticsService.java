package ru.practicum.explorewithme.service;


import ru.practicum.explorewithme.dto.AddHitDto;
import ru.practicum.explorewithme.dto.ResponseStatsDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatisticsService {
    void addHit(AddHitDto addHitDto);

    Collection<ResponseStatsDto> getStats(
            LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            Boolean unique
    );
}
