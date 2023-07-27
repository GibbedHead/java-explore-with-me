package ru.practicum.explorewithme.statsserver.service;


import ru.practicum.explorewithme.statsdto.AddHitDto;
import ru.practicum.explorewithme.statsdto.ResponseStatsDto;

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
