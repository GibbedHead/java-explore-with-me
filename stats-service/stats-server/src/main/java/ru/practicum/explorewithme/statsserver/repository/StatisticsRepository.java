package ru.practicum.explorewithme.statsserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.statsdto.ResponseStatsDto;
import ru.practicum.explorewithme.statsserver.model.Statistics;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    @Query("SELECT NEW ru.practicum.explorewithme.dto.ResponseStatsDto(s.app, s.uri, " +
            "CASE WHEN :unique = true THEN COUNT(DISTINCT s.ip) ELSE COUNT(s.id) END as hits) " +
            "FROM Statistics s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "AND (COALESCE(:uris, NULL) IS NULL OR s.uri IN (:uris)) " +
            "GROUP BY s.app, s.uri " +
            " ORDER BY hits DESC")
    Collection<ResponseStatsDto> getStatsBetweenDatesAndUris(@Param("start") LocalDateTime start,
                                                             @Param("end") LocalDateTime end,
                                                             @Param("uris") List<String> uris,
                                                             @Param("unique") boolean unique);

}
