package ru.practicum.event.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record EventsPrivateGetRequest(Long userId, int from, int size) {

    public Pageable getPageable() {
        return PageRequest.of(from, size);
    }
}
