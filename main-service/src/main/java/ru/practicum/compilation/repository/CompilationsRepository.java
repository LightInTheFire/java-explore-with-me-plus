package ru.practicum.compilation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilation.model.Compilation;

import java.util.Optional;

public interface CompilationsRepository extends JpaRepository<Compilation, Long> {

    Page<Compilation> findAllByPinned(boolean pinned, Pageable pageable);

    @EntityGraph(attributePaths = "events")
    Optional<Compilation> findWithEventsById(Long id);

    boolean existsByTitle(String title);
}
