package ru.practicum.event.service;

import java.util.Collection;

import ru.practicum.event.dto.*;

public interface EventService {
    EventFullDto getById(Long eventId);

    Collection<EventShortDto> getEvents(EventsPublicGetRequest getRequest);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateRequest);

    Collection<EventFullDto> getEvents(EventsAdminGetRequest getRequest);

    Collection<EventShortDto> getEvents(EventsPrivateGetRequest getRequest);

    EventFullDto createEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getByUserById(Long userId, Long eventId);

    EventFullDto updateEventByUserById(
            Long userId, Long eventId, UpdateEventUserRequest updateRequest);
}
