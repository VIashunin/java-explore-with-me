package ru.practicum.main.events.common.service;

import ru.practicum.main.events.dto.CommonEventRequest;
import ru.practicum.main.events.dto.EventFullDto;
import ru.practicum.main.events.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CommonEventService {
    List<EventShortDto> getEvents(CommonEventRequest requests, HttpServletRequest request);

    EventFullDto getEventById(int eventId, HttpServletRequest request);
}