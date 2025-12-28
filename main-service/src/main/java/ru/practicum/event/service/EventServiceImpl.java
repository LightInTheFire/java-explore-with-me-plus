package ru.practicum.event.service;

import java.util.Collection;
import java.util.List;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    @Override
    public EventFullDto getById(Long eventId) {
        return null;
    }

    @Override
    public Collection<EventShortDto> getEvents(EventsPublicGetRequest request) {
        return List.of();
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateRequest) {
        return null;
    }

    @Override
    public Collection<EventFullDto> getEvents(EventsAdminGetRequest getRequest) {
        return List.of();
    }
}
