package ru.practicum.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EndpointHitDtoTest {

    private ObjectMapper objectMapper;
    private Validator validator;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Тест сериализации (объект → JSON)")
    void serialize_ShouldConvertToObjectToJson() throws JsonProcessingException {
        LocalDateTime now = LocalDateTime.of(2025, 6, 15, 10, 30, 45);
        EndpointHitDto dto = EndpointHitDto.builder()
                .app("ewm-main-service")
                .uri("/events/1")
                .ip("192.168.1.1")
                .created(now)
                .build();

        String json = objectMapper.writeValueAsString(dto);

        assertNotNull(json);
        assertTrue(json.contains("\"app\":\"ewm-main-service\""));
        assertTrue(json.contains("\"uri\":\"/events/1\""));
        assertTrue(json.contains("\"ip\":\"192.168.1.1\""));
        assertTrue(json.contains("\"timestamp\":\"2025-06-15 10:30:45\""));
    }

    @Test
    @DisplayName("Тест десериализации (JSON → объект)")
    void deserialize_ShouldConvertFromJsonToObject() throws JsonProcessingException {
        String json =
            """
            {
            "app": "stat-service",
              "uri": "/ping",
              "ip": "127.0.0.1",
              "timestamp": "2025-12-17 15:45:30"
              }
            """;

        EndpointHitDto dto = objectMapper.readValue(json, EndpointHitDto.class);

        assertEquals("stat-service", dto.getApp());
        assertEquals("/ping", dto.getUri());
        assertEquals("127.0.0.1", dto.getIp());
        assertEquals(LocalDateTime.of(2025, 12, 17, 15, 45, 30),
                dto.getCreated());
    }

    @Test
    @DisplayName("Тест десериализации с неверным форматом даты")
    void deserialize_WithInvalidDateFormat_ShouldThrowException() {
        String json =
              """
              {
              "app": "service",
              "uri": "/test",
              "ip": "1.2.3.4",
              "timestamp": "17-12-2025 15:45"
              }
            """;

        assertThrows(JsonProcessingException.class,
                () -> objectMapper.readValue(json, EndpointHitDto.class));
    }

    @Test
    @DisplayName("Тест валидации: все поля заполнены")
    void validate_WithValidData_ShouldHaveNoViolations() {
        EndpointHitDto dto = EndpointHitDto.builder()
                .app("app")
                .uri("/uri")
                .ip("1.2.3.4")
                .created(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<EndpointHitDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Тест валидации: app пустой")
    void validate_WithBlankApp_ShouldHaveViolation() {
        EndpointHitDto dto = EndpointHitDto.builder()
                .app("")
                .uri("/uri")
                .ip("1.2.3.4")
                .created(LocalDateTime.now())
                .build();

        Set<ConstraintViolation<EndpointHitDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("app")));
        assertTrue(violations.stream()
                .anyMatch(v ->
                        v.getMessage().contains("Идентификатор сервиса не может быть пустым")));
    }

    @Test
    @DisplayName("Тест валидации: timestamp null")
    void validate_WithNullTimestamp_ShouldHaveViolation() {
        EndpointHitDto dto = EndpointHitDto.builder()
                .app("app")
                .uri("/uri")
                .ip("1.2.3.4")
                .created(null)
                .build();

        Set<ConstraintViolation<EndpointHitDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("created")));
        assertTrue(violations.stream()
                .anyMatch(v ->
                        v.getMessage().contains("Дата просмотра не может быть пустой")));
    }

    @Test
    @DisplayName("Тест валидации: все поля null/blank")
    void validate_WithAllFieldsInvalid_ShouldHaveMultipleViolations() {
        EndpointHitDto dto = new EndpointHitDto();

        Set<ConstraintViolation<EndpointHitDto>> violations = validator.validate(dto);

        assertEquals(4, violations.size());
    }
}