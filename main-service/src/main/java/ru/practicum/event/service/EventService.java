package ru.practicum.event.service;

import java.util.Collection;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;

public interface EventService {
    EventFullDto getById(Long eventId);

    Collection<EventShortDto> getEvents(EventsPublicGetRequest request);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateRequest);

    Collection<EventFullDto> getEvents(EventsAdminGetRequest getRequest);
}
