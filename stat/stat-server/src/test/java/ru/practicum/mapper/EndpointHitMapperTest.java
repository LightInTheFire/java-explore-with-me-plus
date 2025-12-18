package ru.practicum.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EndpointHitMapperTest {

    @Test
    @DisplayName("Маппинг из EndpointHitDto в EndpointHit")
    void mapToEndpointHit_ShouldConvertDtoToEntityCorrectly() {
        LocalDateTime created = LocalDateTime.of(2025, 6, 15, 10, 30, 45);
        EndpointHitDto dto = new EndpointHitDto("stat-service", "/ping", "192.168.1.1", created);

        EndpointHit entity = EndpointHitMapper.mapToEndpointHit(dto);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("stat-service", entity.getApp());
        assertEquals("/ping", entity.getUri());
        assertEquals("192.168.1.1", entity.getIp());
        assertEquals(created, entity.getCreated());
    }
}