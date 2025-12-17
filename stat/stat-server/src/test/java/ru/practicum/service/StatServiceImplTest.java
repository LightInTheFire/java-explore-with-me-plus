package ru.practicum.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.exceptions.IllegalArgumentException;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatServiceImplTest {

    @Mock
    private StatRepository statRepository;

    @InjectMocks
    private StatServiceImpl statService;

    @Test
    @DisplayName("Тест createEndpointHit с валидным Dto")
    void createEndpointHit_WithValidDto_ShouldSaveAndReturnMessage() {
        EndpointHitDto dto = new EndpointHitDto();
        dto.setApp("app");
        dto.setUri("/uri");
        dto.setIp("1.2.3.4");
        dto.setCreated(LocalDateTime.now());

        String result = statService.createEndpointHit(dto);

        assertEquals("Информация сохранена", result);
        verify(statRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Тест getStat с валидными параметрами и unique=true")
    void getStat_WithValidParamsAndUniqueTrue_ShouldCallUniqueRepoMethod() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 12, 0);
        List<String> uris = List.of("/uri1", "/uri2");
        ViewStatsDto viewStatsDto = new ViewStatsDto("app", "/uri1", 5L);
        when(statRepository.getUniqueStatsWithUris(start, end, uris))
                .thenReturn(Collections.singletonList(viewStatsDto));

        var result = statService.getStat(start, end, uris, true);

        assertEquals(1, result.size());
        assertEquals(viewStatsDto, result.iterator().next());
        verify(statRepository, times(1)).getUniqueStatsWithUris(start, end, uris);
    }

    @Test
    @DisplayName("Тест getStat с валидными параметрами и unique=false")
    void getStat_WithValidParamsAndUniqueFalse_ShouldCallNotUniqueRepoMethod() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 12, 0);
        List<String> uris = List.of("/uri1");
        ViewStatsDto viewStatsDto = new ViewStatsDto("app", "/uri1", 10L);
        when(statRepository.getNotUniqueStatsWithUris(start, end, uris))
                .thenReturn(Collections.singletonList(viewStatsDto));

        var result = statService.getStat(start, end, uris, false);

        assertEquals(1, result.size());
        assertEquals(viewStatsDto, result.iterator().next());
        verify(statRepository, times(1)).getNotUniqueStatsWithUris(start, end, uris);
    }

    @Test
    @DisplayName("Тест getStat с нулевой датой начала")
    void getStat_WithNullStart_ShouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> statService.getStat(null, LocalDateTime.now(), null, false)
        );
        assertEquals("Даты должны быть определены", exception.getMessage());
    }

    @Test
    @DisplayName("Тест getStat с нулевой датой конца")
    void getStat_WithNullEnd_ShouldThrowIllegalArgumentException() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> statService.getStat(LocalDateTime.now(), null, null, false)
        );
        assertEquals("Даты должны быть определены", exception.getMessage());
    }

    @Test
    @DisplayName("Тест getStat с некорректными датами - начало позже конца")
    void getStat_WithEndBeforeStart_ShouldThrowIllegalArgumentException() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 2, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 10, 0);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> statService.getStat(start, end, null, false)
        );
        assertEquals("Дата начала должна быть раньше даты конца", exception.getMessage());
    }

    @Test
    @DisplayName("Тест getStat с некорректными датами - начало равно концу")
    void getStat_WithEndEqualToStart_ShouldThrowIllegalArgumentException() {
        LocalDateTime time = LocalDateTime.of(2025, 1, 1, 10, 0);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> statService.getStat(time, time, null, false)
        );
        assertEquals("Дата начала должна быть раньше даты конца", exception.getMessage());
    }

    @Test
    @DisplayName("Тест getStat с пустым списком uris и unique=true")
    void getStat_WithEmptyUrisAndUniqueTrue_ShouldCallRepoWithUris() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 12, 0);
        List<String> uris = Collections.emptyList(); // пустой список
        ViewStatsDto dto = new ViewStatsDto("app", "/uri1", 5L);
        when(statRepository.getUniqueStatsWithUris(start, end, uris))
                .thenReturn(Collections.singletonList(dto));

        var result = statService.getStat(start, end, uris, true);

        assertEquals(1, result.size());
        assertEquals(dto, result.iterator().next());
        verify(statRepository).getUniqueStatsWithUris(start, end, uris);
    }

    @Test
    @DisplayName("Тест getStat с пустым списком uris и unique=false")
    void getStat_WithEmptyUrisAndUniqueFalse_ShouldCallRepoWithUris() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 12, 0);
        List<String> uris = Collections.emptyList();
        ViewStatsDto dto = new ViewStatsDto("app", "/uri1", 10L);
        when(statRepository.getNotUniqueStatsWithUris(start, end, uris))
                .thenReturn(Collections.singletonList(dto));

        var result = statService.getStat(start, end, uris, false);

        assertEquals(1, result.size());
        assertEquals(dto, result.iterator().next());
        verify(statRepository).getNotUniqueStatsWithUris(start, end, uris);
    }

    @Test
    @DisplayName("Тест getStat с null uris и unique=true")
    void getStat_WithNullUrisAndUniqueTrue_ShouldCallRepoWithoutUris() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 12, 0);
        ViewStatsDto dto = new ViewStatsDto("app", "/uri1", 5L);
        when(statRepository.getUniqueStatsWithoutUris(start, end))
                .thenReturn(Collections.singletonList(dto));

        var result = statService.getStat(start, end, null, true);

        assertEquals(1, result.size());
        assertEquals(dto, result.iterator().next());
        verify(statRepository).getUniqueStatsWithoutUris(start, end);
    }

    @Test
    @DisplayName("Тест getStat с null uris и unique=false")
    void getStat_WithNullUrisAndUniqueFalse_ShouldCallRepoWithoutUris() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 1, 12, 0);
        ViewStatsDto dto = new ViewStatsDto("app", "/uri1", 10L);
        when(statRepository.getNotUniqueStatsWithoutUris(start, end))
                .thenReturn(Collections.singletonList(dto));

        var result = statService.getStat(start, end, null, false);

        assertEquals(1, result.size());
        assertEquals(dto, result.iterator().next());
        verify(statRepository).getNotUniqueStatsWithoutUris(start, end);
    }
}