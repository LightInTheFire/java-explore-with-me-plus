package ru.practicum.comment.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

public record NewCommentDto(@NotBlank @Max(500) String text) {}
