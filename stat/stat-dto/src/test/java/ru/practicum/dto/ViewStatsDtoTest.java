package ru.practicum.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewStatsDtoTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Тест сериализации: объект → JSON")
    void serialize_ShouldConvertToObjectToJson() throws JsonProcessingException {
        ViewStatsDto dto = new ViewStatsDto("main-service", "/events", 42L);

        String json = objectMapper.writeValueAsString(dto);

        assertNotNull(json);
        assertTrue(json.contains("\"app\":\"main-service\""));
        assertTrue(json.contains("\"uri\":\"/events\""));
        assertTrue(json.contains("\"hits\":42"));
    }

    @Test
    @DisplayName("Тест десериализации: JSON → объект")
    void deserialize_ShouldConvertFromJsonToObject() throws JsonProcessingException {
        String json = "{\"app\": \"main-service\", \"uri\": \"/events\", \"hits\": 150}";

        ViewStatsDto dto = objectMapper.readValue(json, ViewStatsDto.class);

        assertEquals("main-service", dto.getApp());
        assertEquals("/events", dto.getUri());
        assertEquals(150L, dto.getHits());
    }

    @Test
    @DisplayName("Тест на обратимость")
    void roundTripSerialization_ShouldPreserveData() throws JsonProcessingException {
        ViewStatsDto original = new ViewStatsDto("main-service", "/events", 100L);

        String json = objectMapper.writeValueAsString(original);
        ViewStatsDto deserialized = objectMapper.readValue(json, ViewStatsDto.class);

        assertEquals(original.getApp(), deserialized.getApp());
        assertEquals(original.getUri(), deserialized.getUri());
        assertEquals(original.getHits(), deserialized.getHits());
    }
}