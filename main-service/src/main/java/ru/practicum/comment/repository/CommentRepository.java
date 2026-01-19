package ru.practicum.comment.repository;

import java.util.List;

import ru.practicum.comment.model.Comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = "author")
    Page<Comment> findAllByEventId(Long eventId, Pageable pageable);

    List<Comment> findAllByAuthorId(Long authorId, Pageable pageable);
}
