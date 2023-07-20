package ru.practicum.main.requests.individual.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.events.individual.service.IndividualEventService;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.model.EventStatus;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.requests.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.main.requests.dto.ParticipationRequestDto;
import ru.practicum.main.requests.model.ParticipationRequest;
import ru.practicum.main.requests.model.StatusEventRequestUpdateResult;
import ru.practicum.main.requests.repository.RequestRepository;
import ru.practicum.main.users.admin.service.AdminUserService;
import ru.practicum.main.users.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main.requests.mapper.RequestMapper.*;

@Service
@Transactional
@RequiredArgsConstructor
public class IndividualRequestServiceImpl implements IndividualRequestService {
    private final RequestRepository requestRepository;
    private final AdminUserService adminUserService;
    private final IndividualEventService individualEventService;

    @Override
    public List<ParticipationRequestDto> getRequestsByUserOtherEvents(int userId) {
        adminUserService.getUserById(userId);
        return mapFromParticipationRequestListToParticipationRequestDtoList(requestRepository.findParticipationRequestsByRequester_Id(userId));
    }

    @Override
    public ParticipationRequestDto createRequestByUserOtherEvents(int userId, int eventId) {
        User user = adminUserService.getUserById(userId);
        Event event = individualEventService.getEventById(eventId);
        List<ParticipationRequestDto> requestDtoList = getRequestsByUserOtherEvents(userId);
        boolean firstCondition = false;
        if (requestDtoList != null && !requestDtoList.isEmpty()) {
            for (ParticipationRequestDto requestDto : requestDtoList) {
                if (requestDto.getEvent() == eventId) {
                    firstCondition = true;
                    break;
                }
            }
        }
        boolean secondCondition = event.getInitiator().getId() == user.getId();
        boolean thirdCondition = event.getState().equals(EventStatus.PENDING) || event.getState().equals(EventStatus.CANCELED);
        boolean fourthCondition = (event.getConfirmedRequests() >= event.getParticipantLimit()) && event.getParticipantLimit() != 0;
        boolean fifthCondition = !event.isRequestModeration();
        boolean sixthCondition = event.getParticipantLimit() == 0;
        if (firstCondition || secondCondition || thirdCondition || fourthCondition) {
            throw new ConflictException("Data integrity has occurred.");
        }
        ParticipationRequest request = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .eventsWithRequests(event)
                .requester(user)
                .build();
        if (fifthCondition || sixthCondition) {
            request.setStatus(StatusEventRequestUpdateResult.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        } else {
            request.setStatus(StatusEventRequestUpdateResult.PENDING);
        }
        return mapFromParticipationRequestToParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequestByUserOtherEvents(int userId, int requestId) {
        ParticipationRequest request = requestRepository.findParticipationRequestByIdAndRequester_Id(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Request is not found or unavailable."));
        request.setStatus(StatusEventRequestUpdateResult.CANCELED);
        return mapFromParticipationRequestToParticipationRequestDto(requestRepository.save(request));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getRequestsByUser(int userId, int eventId) {
        return mapFromParticipationRequestListToParticipationRequestDtoList(requestRepository
                .findParticipationRequestsByEventsWithRequests_IdAndEventsWithRequests_Initiator_Id(eventId, userId));
    }

    @Override
    public EventRequestStatusUpdateResult changeStatusRequestsByUser(int userId, int eventId,
                                                                     EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        List<ParticipationRequest> requestList = requestRepository
                .findParticipationRequestsByEventsWithRequests_IdAndEventsWithRequests_Initiator_Id(eventId, userId);
        if (requestList.isEmpty()) {
            throw new NotFoundException("Request is not found or unavailable.");
        }
        for (ParticipationRequest request : requestList) {
            boolean firstCondition = (request.getEventsWithRequests().getParticipantLimit() == 0) || !request.getEventsWithRequests().isRequestModeration();
            boolean secondCondition = request.getEventsWithRequests().getConfirmedRequests() >= request.getEventsWithRequests().getParticipantLimit();
            boolean thirdCondition = request.getStatus().equals(StatusEventRequestUpdateResult.PENDING);
            if (firstCondition) {
                request.setStatus(StatusEventRequestUpdateResult.CONFIRMED);
                request.getEventsWithRequests().setConfirmedRequests(request.getEventsWithRequests().getConfirmedRequests() + 1);
                requestRepository.save(request);
            }
            if (thirdCondition) {
                if (secondCondition) {
                    request.setStatus(StatusEventRequestUpdateResult.REJECTED);
                    requestRepository.save(request);
                } else {
                    request.setStatus(eventRequestStatusUpdateRequest.getStatus());
                    if (request.getStatus().equals(StatusEventRequestUpdateResult.CONFIRMED)) {
                        request.getEventsWithRequests().setConfirmedRequests(request.getEventsWithRequests().getConfirmedRequests() + 1);
                    }
                    requestRepository.save(request);
                }
            } else {
                throw new ConflictException("Data integrity has occurred.");
            }
        }
        return mapFromParticipationRequestListToEventRequestStatusUpdateResult(requestList);
    }
}
