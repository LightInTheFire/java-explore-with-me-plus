package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentRequestDto;

public record CommentsCreateRequest(long userId, long eventId, CommentRequestDto newComment) {}
