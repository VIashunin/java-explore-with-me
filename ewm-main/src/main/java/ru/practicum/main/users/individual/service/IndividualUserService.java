package ru.practicum.main.users.individual.service;

import ru.practicum.main.events.dto.EventShortDto;
import ru.practicum.main.users.dto.UserDto;

import java.util.List;

public interface IndividualUserService {
    UserDto subscribe(int userId, int subsId);

    UserDto unsubscribe(int userId, int subsId);

    List<EventShortDto> getSubscriptionEvents(int userId, int from, int size);

    List<EventShortDto> getSubscriptionByIdEvents(int userId, int subsId, int from, int size);

    List<EventShortDto> getSubscriberEvents(int userId, int from, int size);

    List<EventShortDto> getSubscriberByIdEvents(int userId, int subsId, int from, int size);
}
