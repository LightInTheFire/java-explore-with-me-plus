package ru.practicum.comment.repository;

import ru.practicum.comment.model.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {}
