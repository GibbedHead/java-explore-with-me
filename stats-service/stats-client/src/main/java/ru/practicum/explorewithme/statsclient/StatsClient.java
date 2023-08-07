package ru.practicum.explorewithme.statsclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.explorewithme.statsdto.dto.AddHitDto;
import ru.practicum.explorewithme.statsdto.dto.ResponseStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class StatsClient {
    private static final String HIT_URI = "/hit";
    private static final String GET_STATS_URI = "/stats";
    @Value("${stats.service.server.url}")
    private String statsServerUrl;

    public void addHit(AddHitDto addHitDto) {
        log.info("Statistics client to add hit  with dto: {}", addHitDto);
        WebClient webClient = WebClient.create(statsServerUrl);
        webClient
                .post()
                .uri(HIT_URI)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(addHitDto))
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> log.error("An error has occurred during add hit request {}", error.getMessage()))
                .block();
    }

    public Collection<ResponseStatsDto> getStats(
            LocalDateTime start,
            LocalDateTime end,
            List<String> uris,
            Boolean unique
    ) {
        log.info(
                "Statistics client call to get statistics .\n\tFrom: {}\n\tTo: {}\n\tUris: {}\n\tUnique: {}",
                start,
                end,
                uris,
                unique
        );
        WebClient webClient = WebClient.create(statsServerUrl);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromPath(GET_STATS_URI);
        if (start != null) {
            uriBuilder.queryParam("start", start.format(dateTimeFormatter));
        }
        if (end != null) {
            uriBuilder.queryParam("end", end.format(dateTimeFormatter));
        }
        if (uris != null && !uris.isEmpty()) {
            uriBuilder.queryParam("uris", uris);
        }
        if (unique != null) {
            uriBuilder.queryParam("unique", unique);
        }
        return webClient
                .get()
                .uri(uriBuilder.build().toUriString())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Collection<ResponseStatsDto>>() {
                })
                .block();
    }
}
