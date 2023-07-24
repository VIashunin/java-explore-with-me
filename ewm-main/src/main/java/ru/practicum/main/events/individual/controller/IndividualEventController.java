package ru.practicum.main.events.individual.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.events.dto.EventFullDto;
import ru.practicum.main.events.dto.EventShortDto;
import ru.practicum.main.events.dto.NewEventDto;
import ru.practicum.main.events.dto.UpdateEventUserRequest;
import ru.practicum.main.events.individual.service.IndividualEventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class IndividualEventController {
    private final IndividualEventService individualEventService;

    @GetMapping(path = "/{userId}/events")
    public List<EventShortDto> getEventsByUser(@PathVariable int userId,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        return individualEventService.getEventsByUser(userId, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/{userId}/events")
    public EventFullDto createEvent(@PathVariable int userId,
                                    @RequestBody @Valid NewEventDto newEventDto) {
        return individualEventService.createEvent(userId, newEventDto);
    }

    @GetMapping(path = "/{userId}/events/{eventId}")
    public EventFullDto getEventByUserFullInfo(@PathVariable int userId,
                                               @PathVariable int eventId) {
        return individualEventService.getEventByUserFullInfo(userId, eventId);
    }

    @PatchMapping(path = "/{userId}/events/{eventId}")
    public EventFullDto changeEventByUser(@PathVariable int userId,
                                          @PathVariable int eventId,
                                          @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {
        return individualEventService.changeEventByUser(userId, eventId, updateEventUserRequest);
    }
}
