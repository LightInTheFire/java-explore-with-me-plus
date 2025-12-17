package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitDto {
    @NotBlank(message = "Идентификатор сервиса не может быть пустым")
    String app;

    @NotBlank(message = "URI не может быть пустым")
    String uri;

    @NotBlank(message = "Ip не может быть пустым")
    String ip;

    @NotNull(message = "Дата просмотра не может быть пустой")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("timestamp")
    LocalDateTime created;
}