package ru.practicum.comment.service;

import java.util.Collection;

import ru.practicum.comment.dto.CommentDto;

public interface CommentService {
    Collection<CommentDto> getAllCommentsPaged(CommentsPublicGetRequest request);

    Collection<CommentDto> getAllCommentsPaged(CommentsPrivateGetRequest request);

    void deleteComment(long eventId, long commentId);

    void deleteCommentByUser(long userId, long commentId);

    CommentDto createComment(CommentsCreateRequest request);

    CommentDto updateComment(CommentsUpdateRequest request);
}
