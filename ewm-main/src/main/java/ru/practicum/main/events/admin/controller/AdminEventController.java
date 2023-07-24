package ru.practicum.main.events.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.events.admin.service.AdminEventService;
import ru.practicum.main.events.dto.AdminEventRequest;
import ru.practicum.main.events.dto.EventFullDto;
import ru.practicum.main.events.dto.UpdateEventAdminRequest;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class AdminEventController {
    private final AdminEventService adminEventService;

    @GetMapping(path = "/events")
    public List<EventFullDto> findEvents(@RequestParam(required = false) List<Integer> users,
                                         @RequestParam(required = false) List<String> states,
                                         @RequestParam(required = false) List<Integer> categories,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(defaultValue = "0") int from,
                                         @RequestParam(defaultValue = "10") int size) {
        return adminEventService.findEvents(AdminEventRequest.of(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping(path = "/events/{eventId}")
    public EventFullDto changeEvents(@PathVariable int eventId,
                                     @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        return adminEventService.changeEvent(eventId, updateEventAdminRequest);
    }
}
