package ru.practicum.comment.service;

import ru.practicum.comment.dto.NewCommentDto;

public record CommentsCreateRequest(long userId, long eventId, NewCommentDto newComment) {}
