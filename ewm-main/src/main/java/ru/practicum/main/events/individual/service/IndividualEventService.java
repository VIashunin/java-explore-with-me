package ru.practicum.main.events.individual.service;

import ru.practicum.main.events.dto.EventFullDto;
import ru.practicum.main.events.dto.EventShortDto;
import ru.practicum.main.events.dto.NewEventDto;
import ru.practicum.main.events.dto.UpdateEventUserRequest;
import ru.practicum.main.events.model.Event;

import java.util.List;

public interface IndividualEventService {
    List<EventShortDto> getEventsByUser(int userId, int from, int size);

    EventFullDto createEvent(int userId, NewEventDto newEventDto);

    EventFullDto getEventByUserFullInfo(int userId, int eventId);

    EventFullDto changeEventByUser(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest);

    Event getEventById(int eventId);
}
