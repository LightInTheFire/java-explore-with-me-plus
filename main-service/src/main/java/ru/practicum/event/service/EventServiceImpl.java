package ru.practicum.event.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.*;

import jakarta.servlet.http.HttpServletRequest;

import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.request.StatsClient;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.event.controller.EventSortBy;
import ru.practicum.event.dto.*;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.mapper.LocationMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Location;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ForbiddenAccessException;
import ru.practicum.exception.IllegalEventUpdateException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private static final Duration MIN_TIME_BEFORE_EVENT = Duration.ofHours(2);
    private static final LocalDateTime MINIMAL_LOCAL_DATE_TIME =
            LocalDateTime.of(1000, 1, 1, 0, 0, 0);
    private static final LocalDateTime MAXIMUM_LOCAL_DATE_TIME =
            LocalDateTime.of(9999, 1, 1, 0, 0, 0);
    private static final String EVENTS_URI = "/events/%d";
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;

    @Override
    public EventFullDto getById(Long eventId, HttpServletRequest request) {
        Optional<Event> eventOptional =
                eventRepository.findByIdAndState(eventId, EventState.PUBLISHED);
        Event event =
                eventOptional.orElseThrow(
                        NotFoundException.supplier("Event with id=%d not found", eventId));

        statsClient.hit(request);
        String uri = request.getRequestURI();

        ViewStatsDto statsDto = getStatsForEvent(event, uri);

        return EventMapper.mapToFullDto(
                event, 0, statsDto.hits()); // change 0 to actual number of requests
    }

    @Override
    public Collection<EventShortDto> getEvents(EventsPublicGetRequest getRequest) {
        Page<Event> events =
                eventRepository.findAll(
                        EventRepository.createPredicate(getRequest), getRequest.getPageable());

        statsClient.hit(getRequest.httpRequest());

        LocalDateTime statsFrom =
                getRequest.hasRangeStart() ? getRequest.rangeStart() : LocalDateTime.now();
        LocalDateTime statsTo =
                getRequest.hasRangeEnd() ? getRequest.rangeEnd() : MAXIMUM_LOCAL_DATE_TIME;

        Map<Long, Long> statsForEvents = getStatsMapForEvents(events, statsFrom, statsTo);

        List<EventShortDto> eventsList =
                events.stream()
                        .map(
                                event ->
                                        EventMapper.mapToShortDto(
                                                event,
                                                0,
                                                statsForEvents.get(
                                                        event.getId()))) // change 0 to actual
                        // number of requests
                        .toList();

        if (EventSortBy.VIEWS.equals(getRequest.sort())) {
            return eventsList.stream().sorted(Comparator.comparing(EventShortDto::views)).toList();
        }

        return eventsList;
    }

    @Override
    public Collection<EventFullDto> getEvents(EventsAdminGetRequest getRequest) {
        Page<Event> events =
                eventRepository.findAll(
                        EventRepository.createPredicate(getRequest), getRequest.getPageable());

        Map<Long, Long> statsForEvents =
                getStatsMapForEvents(events, MINIMAL_LOCAL_DATE_TIME, MAXIMUM_LOCAL_DATE_TIME);

        return events.stream()
                .map(
                        event ->
                                EventMapper.mapToFullDto(
                                        event,
                                        0,
                                        statsForEvents.get(
                                                event.getId()))) // change 0 to actual number of
                // requests
                .toList();
    }

    @Override
    public Collection<EventShortDto> getEvents(EventsPrivateGetRequest getRequest) {
        getUserByIdOrThrow(getRequest.userId());
        Page<Event> events =
                eventRepository.findByInitiator_Id(getRequest.userId(), getRequest.getPageable());

        Map<Long, Long> statsForEvents =
                getStatsMapForEvents(events, MINIMAL_LOCAL_DATE_TIME, MAXIMUM_LOCAL_DATE_TIME);

        return events.stream()
                .map(
                        event ->
                                EventMapper.mapToShortDto(
                                        event,
                                        0,
                                        statsForEvents.get(
                                                event.getId()))) // change 0 to actual number of
                // requests
                .toList();
    }

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        Location location = LocationMapper.mapToEntity(newEventDto.location());
        Category category = getCategoryByIdOrThrow(newEventDto.category());
        User initiator = getUserByIdOrThrow(userId);
        Event event = EventMapper.mapToEntity(newEventDto, category, initiator, location);

        LocalDateTime now = LocalDateTime.now();
        if (event.getEventDate().isBefore(now.plus(MIN_TIME_BEFORE_EVENT))) {
            throw new ValidationException(
                    "The event must be scheduled at least %d hours from now."
                            .formatted(MIN_TIME_BEFORE_EVENT.toHours()));
        }
        Event saved = eventRepository.save(event);

        return EventMapper.mapToFullDto(saved, 0, 0L);
    }

    @Override
    public EventFullDto getByUserById(Long userId, Long eventId) {
        User user = getUserByIdOrThrow(userId);

        Event event = getEventByIdOrThrow(eventId);

        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new ForbiddenAccessException("You can't view event that's not yours");
        }

        ViewStatsDto statsDto = getStatsForEvent(event, EVENTS_URI.formatted(eventId));

        return EventMapper.mapToFullDto(
                event, 0, statsDto.hits()); // change 0 to actual number of requests
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateRequest) {
        Event event = getEventByIdOrThrow(eventId);

        if ((event.getState().equals(EventState.PUBLISHED)
                        || event.getState().equals(EventState.CANCELED))
                && updateRequest.hasStateAction()) {
            throw new IllegalEventUpdateException(
                    "Forbidden to update event that already %s"
                            .formatted(event.getState().toString()));
        }

        Category newCategory = null;
        if (updateRequest.hasCategory()) {
            newCategory = getCategoryByIdOrThrow(updateRequest.category());
        }
        EventMapper.updateEventFromDto(event, updateRequest, newCategory);

        Event saved = eventRepository.save(event);

        return EventMapper.mapToFullDto(saved, 0, null); // change 0 to actual number of requests
    }

    @Override
    @Transactional
    public EventFullDto updateEventByUser(
            Long userId, Long eventId, UpdateEventUserRequest updateRequest) {
        Event event = getEventByIdOrThrow(eventId);
        User user = getUserByIdOrThrow(userId);

        if (!event.getInitiator().getId().equals(user.getId())) {
            throw new ForbiddenAccessException("You can't update event that's not yours");
        }

        if ((event.getState().equals(EventState.PUBLISHED)
                        || event.getState().equals(EventState.CANCELED))
                && !updateRequest.hasStateAction()) {
            throw new IllegalEventUpdateException(
                    "Forbidden to update event that already %s"
                            .formatted(event.getState().toString()));
        }

        Category newCategory = null;
        if (updateRequest.hasCategory()) {
            newCategory = getCategoryByIdOrThrow(updateRequest.category());
        }
        EventMapper.updateEventFromDto(event, updateRequest, newCategory);

        Event saved = eventRepository.save(event);

        return EventMapper.mapToFullDto(saved, 0, null); // change 0 to actual number of requests
    }

    private Map<Long, Long> getStatsMapForEvents(
            Page<Event> events, LocalDateTime from, LocalDateTime to) {
        List<String> listOfUris =
                events.stream().map(event -> EVENTS_URI.formatted(event.getId())).toList();
        return getStatsForEvents(listOfUris, from, to).stream()
                .collect(
                        Collectors.toMap(
                                statsDto ->
                                        Long.valueOf(
                                                statsDto.uri()
                                                        .substring(
                                                                statsDto.uri().lastIndexOf('/')
                                                                        + 1)),
                                ViewStatsDto::hits));
    }

    private List<ViewStatsDto> getStatsForEvents(
            List<String> uris, LocalDateTime from, LocalDateTime to) {
        try {
            return statsClient.getStats(from, to, uris, true);
        } catch (Exception e) {
            log.error("Error during getting stats for events", e);
        }
        return List.of();
    }

    private ViewStatsDto getStatsForEvent(Event event, String uri) {
        ViewStatsDto statsDto;
        try {
            statsDto =
                    statsClient
                            .getStats(
                                    MINIMAL_LOCAL_DATE_TIME,
                                    MAXIMUM_LOCAL_DATE_TIME,
                                    List.of(uri),
                                    true)
                            .getFirst();
        } catch (NoSuchElementException e) {
            log.trace("No stats for event with id={} found", event.getId());
            statsDto = new ViewStatsDto(null, null, 0L);
        } catch (Exception e) {
            log.error("Error during getting stats for event with id={}", event.getId(), e);
            statsDto = new ViewStatsDto(null, null, null);
        }
        return statsDto;
    }

    private Event getEventByIdOrThrow(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        return eventOptional.orElseThrow(
                NotFoundException.supplier("Event with id=%d not found", eventId));
    }

    private User getUserByIdOrThrow(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.orElseThrow(
                NotFoundException.supplier("User with id=%d not found", userId));
    }

    private Category getCategoryByIdOrThrow(Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        return optionalCategory.orElseThrow(
                NotFoundException.supplier("Category with id=%d not found", categoryId));
    }
}
