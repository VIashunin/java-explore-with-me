package ru.practicum.main.requests.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.main.requests.model.StatusEventRequestUpdateResult;

@Data
@Builder
public class ParticipationRequestDto {
    private int id;
    private String created;
    private int event;
    private int requester;
    private StatusEventRequestUpdateResult status;
}
