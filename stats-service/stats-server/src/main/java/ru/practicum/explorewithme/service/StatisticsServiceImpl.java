package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.AddHitDto;
import ru.practicum.explorewithme.dto.ResponseStatsDto;
import ru.practicum.explorewithme.mapper.StatisticsMapper;
import ru.practicum.explorewithme.repository.StatisticsRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final StatisticsMapper statisticsMapper = Mappers.getMapper(StatisticsMapper.class);

    @Override
    public void addHit(AddHitDto addHitDto) {
        statisticsRepository.save(
                statisticsMapper.AddHitToStatistics(addHitDto)
        );
    }

    @Override
    public Collection<ResponseStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        return statisticsRepository.getStatsBetweenDatesAndUris(start, end, uris, unique);
    }
}
