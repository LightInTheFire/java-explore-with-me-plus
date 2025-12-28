package ru.practicum.event.controller;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.EventService;
import ru.practicum.event.service.EventsPublicGetRequest;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventPublicController {
    private final EventService eventService;

    @GetMapping()
    public Collection<EventShortDto> getEventsFiltered(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) LocalDateTime rangeStart,
            @RequestParam(required = false) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") boolean onlyAvailable,
            @RequestParam(required = false) EventSortBy sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        EventsPublicGetRequest getRequest =
                new EventsPublicGetRequest(
                        text,
                        categories,
                        paid,
                        rangeStart,
                        rangeEnd,
                        onlyAvailable,
                        sort,
                        from,
                        size);
        log.info("Public get events requested with params= {}", getRequest);
        return eventService.getEvents(getRequest);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable Long eventId) {
        log.info("Public get event with eventId={} requested", eventId);
        return eventService.getById(eventId);
    }
}
