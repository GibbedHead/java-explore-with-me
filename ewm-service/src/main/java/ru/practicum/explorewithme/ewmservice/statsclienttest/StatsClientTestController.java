package ru.practicum.explorewithme.ewmservice.statsclienttest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.statsclient.StatsClient;
import ru.practicum.explorewithme.statsdto.dto.AddHitDto;
import ru.practicum.explorewithme.statsdto.dto.ResponseStatsDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsClientTestController {
    private final StatsClient statsClient;

    @GetMapping("/stats-client-test-hit")
    public void testClientHit() {

        String app = "ewm-main-service";
        String uri = "/events/1";
        String ip = "121.0.0.1";
        AddHitDto dtoOld = new AddHitDto(
                app,
                uri,
                ip,
                LocalDateTime.now().minusYears(1)
        );
        AddHitDto dtoCurrentNonUnique1 = new AddHitDto(
                app,
                uri,
                ip,
                LocalDateTime.now()
        );
        AddHitDto dtoCurrentNonUnique2 = new AddHitDto(
                app,
                uri,
                ip,
                LocalDateTime.now()
        );
        AddHitDto dtoCurrentUnique1 = new AddHitDto(
                app,
                uri,
                "121.0.0.2",
                LocalDateTime.now()
        );
        AddHitDto dtoCurrent2ndUri = new AddHitDto(
                app,
                "/events/2",
                ip,
                LocalDateTime.now()
        );
        AddHitDto dtoFuture = new AddHitDto(
                app,
                uri,
                ip,
                LocalDateTime.now().plusYears(1)
        );
        statsClient.addHit(dtoOld);
        statsClient.addHit(dtoCurrentNonUnique1);
        statsClient.addHit(dtoCurrentNonUnique2);
        statsClient.addHit(dtoCurrentUnique1);
        statsClient.addHit(dtoCurrent2ndUri);
        statsClient.addHit(dtoFuture);
    }

    @GetMapping("/stats-client-test-stat")
    public Collection<ResponseStatsDto> testClientStats() {
        return statsClient.getStats(
                LocalDateTime.now().minusMonths(1),
                LocalDateTime.now().plusMonths(1),
                null,
                null
        );
    }

    @GetMapping("/stats-client-test-unique")
    public Collection<ResponseStatsDto> testClientStatsUnique() {
        return statsClient.getStats(
                LocalDateTime.now().minusMonths(1),
                LocalDateTime.now().plusMonths(1),
                null,
                true
        );
    }

    @GetMapping("/stats-client-test-unique-list")
    public Collection<ResponseStatsDto> testClientStatsUniqueList() {
        return statsClient.getStats(
                LocalDateTime.now().minusMonths(1),
                LocalDateTime.now().plusMonths(1),
                List.of("/events/2"),
                true
        );
    }
}
