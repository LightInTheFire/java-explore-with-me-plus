package ru.practicum.event.controller;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventService;
import ru.practicum.event.service.EventsAdminGetRequest;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    public Collection<EventFullDto> getEventsFiltered(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) LocalDateTime rangeStart,
            @RequestParam(required = false) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        EventsAdminGetRequest getRequest =
                new EventsAdminGetRequest(
                        users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEvents(getRequest);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(
            @PathVariable Long eventId, @RequestBody UpdateEventAdminRequest updateRequest) {
        return eventService.updateEvent(eventId, updateRequest);
    }
}
