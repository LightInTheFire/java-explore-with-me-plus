package ru.practicum.comment.service;

import ru.practicum.comment.dto.NewCommentDto;

public record CommentsUpdateRequest(long userId, long commentId, NewCommentDto updateComment) {}
