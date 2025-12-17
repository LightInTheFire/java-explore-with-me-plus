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
        LocalDateTime timestamp = LocalDateTime.of(2025, 6, 15, 10, 30, 45);
        EndpointHitDto dto = new EndpointHitDto();
        dto.setApp("stat-service");
        dto.setUri("/ping");
        dto.setIp("192.168.1.1");
        dto.setCreated(timestamp);

        EndpointHit entity = EndpointHitMapper.mapToEndpointHit(dto);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("stat-service", entity.getApp());
        assertEquals("/ping", entity.getUri());
        assertEquals("192.168.1.1", entity.getIp());
        assertEquals(timestamp, entity.getCreated());
    }
}