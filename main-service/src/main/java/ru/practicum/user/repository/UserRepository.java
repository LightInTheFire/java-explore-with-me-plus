package ru.practicum.user.repository;

import java.util.Collection;

import ru.practicum.user.model.User;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Collection<User> findAllByIdIn(Collection<Long> ids, Pageable pageable);
}
