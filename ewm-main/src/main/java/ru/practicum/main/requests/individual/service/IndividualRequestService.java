package ru.practicum.main.requests.individual.service;

import ru.practicum.main.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.main.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface IndividualRequestService {
    List<ParticipationRequestDto> getRequestsByUserOtherEvents(int userId);

    ParticipationRequestDto createRequestByUserOtherEvents(int userId, int eventId);

    ParticipationRequestDto cancelRequestByUserOtherEvents(int userId, int requestId);

    List<ParticipationRequestDto> getRequestsByUser(int userId, int eventId);

    EventRequestStatusUpdateResult changeStatusRequestsByUser(int userId, int eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}
