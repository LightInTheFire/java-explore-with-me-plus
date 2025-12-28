package ru.practicum.event.mapper;

import java.time.LocalDateTime;

import ru.practicum.category.model.Category;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Location;
import ru.practicum.user.model.User;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EventMapper {

    public Event mapToEntity(
            NewEventDto newEventDto, Category category, User initiator, Location location) {
        return new Event(
                null,
                newEventDto.annotation(),
                category,
                LocalDateTime.now(),
                newEventDto.description(),
                newEventDto.eventDate(),
                initiator,
                location,
                newEventDto.paid(),
                newEventDto.participantLimit(),
                null,
                newEventDto.requestModeration(),
                EventState.PENDING,
                newEventDto.title());
    }
}
