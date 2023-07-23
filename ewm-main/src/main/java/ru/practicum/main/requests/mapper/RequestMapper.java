package ru.practicum.main.requests.mapper;

import ru.practicum.main.requests.dto.EventRequestStatusUpdateResult;
import ru.practicum.main.requests.dto.ParticipationRequestDto;
import ru.practicum.main.requests.model.ParticipationRequest;
import ru.practicum.main.requests.model.StatusEventRequestUpdateResult;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RequestMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ParticipationRequestDto mapFromParticipationRequestToParticipationRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .created(participationRequest.getCreated().toString().replace("T", " ").substring(0, participationRequest.getCreated().toString().indexOf(".")))
                .event(participationRequest.getEventsWithRequests().getId())
                .requester(participationRequest.getRequester().getId())
                .status(participationRequest.getStatus())
                .build();
    }

    public static List<ParticipationRequestDto> mapFromParticipationRequestListToParticipationRequestDtoList(List<ParticipationRequest> participationRequestList) {
        List<ParticipationRequestDto> participationRequestDtoList = new ArrayList<>();
        for (ParticipationRequest request : participationRequestList) {
            participationRequestDtoList.add(mapFromParticipationRequestToParticipationRequestDto(request));
        }
        return participationRequestDtoList;
    }

    public static EventRequestStatusUpdateResult mapFromParticipationRequestListToEventRequestStatusUpdateResult(List<ParticipationRequest> requestList) {
        List<ParticipationRequestDto> requestsConfirmed = new ArrayList<>();
        List<ParticipationRequestDto> requestsRejected = new ArrayList<>();
        for (ParticipationRequest request : requestList) {
            if (request.getStatus() == StatusEventRequestUpdateResult.CONFIRMED) {
                requestsConfirmed.add(mapFromParticipationRequestToParticipationRequestDto(request));
            }
            if (request.getStatus() == StatusEventRequestUpdateResult.REJECTED) {
                requestsRejected.add(mapFromParticipationRequestToParticipationRequestDto(request));
            }
        }
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(requestsConfirmed)
                .rejectedRequests(requestsRejected)
                .build();
    }
}