package ru.practicum.main.requests.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.main.requests.model.StatusEventRequestUpdateResult;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;
    private StatusEventRequestUpdateResult status;
}
