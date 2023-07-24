package ru.practicum.main.events.admin.service;

import ru.practicum.main.events.dto.AdminEventRequest;
import ru.practicum.main.events.dto.EventFullDto;
import ru.practicum.main.events.dto.UpdateEventAdminRequest;

import java.util.List;

public interface AdminEventService {
    List<EventFullDto> findEvents(AdminEventRequest requests);

    EventFullDto changeEvent(int eventId, UpdateEventAdminRequest updateEventAdminRequest);
}
