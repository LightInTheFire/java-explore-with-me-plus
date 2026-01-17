package ru.practicum.compilation.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public record CompilationsPublicGetRequest(boolean pinned, int from, int size) {

    public Pageable getPageable() {
        int page = from / size;
        return PageRequest.of(page, size);
    }
}
