package ru.practicum.main.events.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main.events.model.EventSort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
public class CommonEventRequest {
    private String text;
    private List<Integer> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private EventSort sortEvents;
    private Integer from;
    private Integer size;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static CommonEventRequest of(String text,
                                        List<Integer> categories,
                                        String paid,
                                        String rangeStart,
                                        String rangeEnd,
                                        String onlyAvailable,
                                        String sort,
                                        Integer from,
                                        Integer size) {
        CommonEventRequest request = new CommonEventRequest();
        request.setText(text);
        if (paid != null) {
            request.setPaid(Boolean.parseBoolean(paid));
        }
        if (rangeStart != null) {
            request.setRangeStart(LocalDateTime.parse(rangeStart, formatter));
        }
        if (rangeEnd != null) {
            request.setRangeEnd(LocalDateTime.parse(rangeEnd, formatter));
        }
        if (onlyAvailable != null) {
            request.setOnlyAvailable(Boolean.parseBoolean(onlyAvailable));
        }
        if (sort != null) {
            request.setSortEvents(EventSort.valueOf(sort.toUpperCase()));
        }
        request.setFrom(from);
        request.setSize(size);
        if (categories != null) {
            request.setCategories(categories);
        }
        return request;
    }

    public boolean hasCategories() {
        return categories != null && !categories.isEmpty();
    }
}
