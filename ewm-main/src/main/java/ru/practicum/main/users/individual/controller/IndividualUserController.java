package ru.practicum.main.users.individual.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.events.dto.EventShortDto;
import ru.practicum.main.users.dto.UserDto;
import ru.practicum.main.users.individual.service.IndividualUserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class IndividualUserController {
    private final IndividualUserService individualUserService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/{userId}/subscribe/{subsId}")
    public UserDto subscribe(@PathVariable int userId,
                             @PathVariable int subsId) {
        return individualUserService.subscribe(userId, subsId);
    }

    @DeleteMapping(path = "/{userId}/unsubscribe/{subsId}")
    public UserDto unsubscribe(@PathVariable int userId,
                               @PathVariable int subsId) {
        return individualUserService.unsubscribe(userId, subsId);
    }

    @GetMapping(path = "/{userId}/subscription/events")
    public List<EventShortDto> getSubscriptionEvents(@PathVariable int userId,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        return individualUserService.getSubscriptionEvents(userId, from, size);
    }

    @GetMapping(path = "/{userId}/subscriber/events")
    public List<EventShortDto> getSubscriberEvents(@PathVariable(name = "userId") int userId,
                                                   @RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") int size) {
        return individualUserService.getSubscriberEvents(userId, from, size);
    }

    @GetMapping(path = "/{userId}/subscription/{subsId}/events")
    public List<EventShortDto> getSubscriptionByIdEvents(@PathVariable int userId,
                                                         @PathVariable int subsId,
                                                         @RequestParam(defaultValue = "0") int from,
                                                         @RequestParam(defaultValue = "10") int size) {
        return individualUserService.getSubscriptionByIdEvents(userId, subsId, from, size);
    }

    @GetMapping(path = "/{userId}/subscriber/{subsId}/events")
    public List<EventShortDto> getSubscriberByIdEvents(@PathVariable int userId,
                                                       @PathVariable int subsId,
                                                       @RequestParam(defaultValue = "0") int from,
                                                       @RequestParam(defaultValue = "10") int size) {
        return individualUserService.getSubscriberByIdEvents(userId, subsId, from, size);
    }
}