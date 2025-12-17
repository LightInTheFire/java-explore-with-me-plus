package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.exceptions.IllegalArgumentException;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static ru.practicum.mapper.EndpointHitMapper.mapToEndpointHit;

@RequiredArgsConstructor
@Service
public class StatServiceImpl implements StatService {
    @Autowired
    private StatRepository statRepository;

    @Override
    public String createEndpointHit(EndpointHitDto endpointHitDto) {
        statRepository.save(mapToEndpointHit(endpointHitDto));
        return "Информация сохранена";
    }

    @Override
    public Collection<ViewStatsDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Даты должны быть определены");
        } else if (!end.isAfter(start)) {
            throw new IllegalArgumentException("Дата начала должна быть раньше даты конца");
        }

        if (!(uris == null)) {
            if (unique) {
                return statRepository.getUniqueStatsWithUris(start, end, uris);
            } else {
                return statRepository.getNotUniqueStatsWithUris(start, end, uris);
            }
        } else {
            if (unique) {
                return statRepository.getUniqueStatsWithoutUris(start, end);
            } else {
                return statRepository.getNotUniqueStatsWithoutUris(start, end);
            }
        }
    }
}
