package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentRequestDto;

public record CommentsUpdateRequest(long userId, long commentId, CommentRequestDto updateComment) {}
