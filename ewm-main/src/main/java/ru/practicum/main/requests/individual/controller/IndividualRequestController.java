package ru.practicum.main.requests.individual.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.main.requests.dto.ParticipationRequestDto;
import ru.practicum.main.requests.individual.service.IndividualRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class IndividualRequestController {
    private final IndividualRequestService individualRequestService;

    @GetMapping(path = "{userId}/requests")
    public List<ParticipationRequestDto> getRequestsByUserOtherEvents(@PathVariable int userId) {
        return individualRequestService.getRequestsByUserOtherEvents(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "{userId}/requests")
    public ParticipationRequestDto createRequestByUserOtherEvents(@PathVariable int userId,
                                                                  @RequestParam int eventId) {
        return individualRequestService.createRequestByUserOtherEvents(userId, eventId);
    }

    @PatchMapping(path = "{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByUserOtherEvents(@PathVariable int userId,
                                                                  @PathVariable int requestId) {
        return individualRequestService.cancelRequestByUserOtherEvents(userId, requestId);
    }

    @GetMapping(path = "{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByUser(@PathVariable int userId,
                                                           @PathVariable int eventId) {
        return individualRequestService.getRequestsByUser(userId, eventId);
    }

    @PatchMapping(path = "{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeStatusRequestsByUser(@PathVariable int userId,
                                                                     @PathVariable int eventId,
                                                                     @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return individualRequestService.changeStatusRequestsByUser(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
