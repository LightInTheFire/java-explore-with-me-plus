package ru.practicum.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewCommentDto(@NotBlank @Size(max = 500) String text) {}
