package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatService {
    String createEndpointHit(EndpointHitDto endpointHitDto);

    Collection<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end,
                                     List<String> uris, Boolean unique);
}
