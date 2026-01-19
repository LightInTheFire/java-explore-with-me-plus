package ru.practicum.comment.service;

import java.util.Collection;
import java.util.List;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.user.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public Collection<CommentDto> getAllCommentsPaged(CommentsPublicGetRequest request) {
        return List.of();
    }

    @Override
    public Collection<CommentDto> getAllCommentsPaged(CommentsPrivateGetRequest request) {
        return List.of();
    }

    @Override
    public void deleteComment(long eventId, long commentId) {}

    @Override
    public void deleteCommentByUser(long userId, long commentId) {}

    @Override
    public CommentDto createComment(CommentsCreateRequest request) {
        return null;
    }

    @Override
    public CommentDto updateComment(CommentsUpdateRequest request) {
        return null;
    }
}
