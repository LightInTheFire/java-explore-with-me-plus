package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Builder
public record EndpointHitDto(@NotBlank(message = "Идентификатор сервиса не может быть пустым") String app,
                             @NotBlank(message = "URI не может быть пустым") String uri,
                             @NotBlank(message = "Ip не может быть пустым") String ip,
                             @NotNull(message = "Дата просмотра не может быть пустой")
                             @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime created) {

}