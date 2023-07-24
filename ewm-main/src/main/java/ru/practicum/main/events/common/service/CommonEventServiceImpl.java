package ru.practicum.main.events.common.service;

import com.google.gson.Gson;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.events.dto.CommonEventRequest;
import ru.practicum.main.events.dto.EventFullDto;
import ru.practicum.main.events.dto.EventShortDto;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.model.EventSort;
import ru.practicum.main.events.model.EventStatus;
import ru.practicum.main.events.model.QEvent;
import ru.practicum.main.events.repository.EventRepository;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.TimeException;
import ru.practicum.main.stats.dto.ResponseDto;
import ru.practicum.main.stats.model.StatsClient;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.practicum.main.events.mapper.EventMapper.mapFromEventListToEventShortDtoList;
import static ru.practicum.main.events.mapper.EventMapper.mapFromEventToEventFullDto;

@Service
@Transactional
@RequiredArgsConstructor
public class CommonEventServiceImpl implements CommonEventService {
    private final EventRepository eventRepository;
    private final StatsClient statsClient;
    private final Gson gson = new Gson();

    @Override
    public List<EventShortDto> getEvents(CommonEventRequest requests, HttpServletRequest request) {
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();
        if (requests.getText() != null) {
            conditions.add(event.annotation.containsIgnoreCase(requests.getText()).or(event.description.containsIgnoreCase(requests.getText())));
        }
        if (requests.hasCategories()) {
            for (Integer id : requests.getCategories()) {
                conditions.add(event.category.id.eq(id));
            }
        }
        if (requests.getPaid() != null) {
            conditions.add(event.paid.eq(requests.getPaid()));
        }
        if (requests.getRangeStart() != null && requests.getRangeEnd() != null) {
            if (requests.getRangeStart().isAfter(requests.getRangeEnd())) {
                throw new TimeException("Data does not meet the creation rules.");
            } else {
                conditions.add(event.eventDate.between(requests.getRangeStart(), requests.getRangeEnd()));
            }
        }
        if (requests.getOnlyAvailable() != null) {
            conditions.add(event.confirmedRequests.loe(event.participantLimit));
        }
        PageRequest pageRequest = PageRequest.of(requests.getFrom(), requests.getSize());
        if (requests.getSortEvents() != null) {
            Sort sort = makeOrderByClause(requests.getSortEvents());
            pageRequest = PageRequest.of(requests.getFrom(), requests.getSize(), sort);
        }
        conditions.add(event.state.eq(EventStatus.PUBLISHED));
        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();
        Page<Event> eventsPage = eventRepository.findAll(finalCondition, pageRequest);
        if (eventsPage.isEmpty()) {
            throw new NotFoundException("Event is not found or unavailable.");
        }
        statsClient.hit(request);
        for (Event eventViewed : eventsPage) {
            eventViewed.setViews(parseViews(eventViewed, request));
        }
        eventRepository.saveAll(eventsPage);
        return mapFromEventListToEventShortDtoList(eventsPage);
    }

    @Override
    public EventFullDto getEventById(int eventId, HttpServletRequest request) {
        Event event = eventRepository.findEventsByIdAndStateIs(eventId, EventStatus.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event is not found or unavailable."));
        statsClient.hit(request);
        event.setViews(parseViews(event, request));
        eventRepository.save(event);
        return mapFromEventToEventFullDto(event);
    }

    private int parseViews(Event event, HttpServletRequest request) {
        ResponseEntity<Object> response = statsClient.stats(event.getCreatedOn().toString().replace("T", " "),
                event.getEventDate().toString().replace("T", " "), List.of(request.getRequestURI()), true);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            String body = Objects.requireNonNull(response.getBody()).toString()
                    .replace("[{", "{\"")
                    .replace("}]", "\"}")
                    .replace("=", "\":\"")
                    .replace(", ", "\",\"");
            ResponseDto responseDto = gson.fromJson(body, ResponseDto.class);
            return responseDto.getHits().intValue();
        }
        return event.getViews();
    }

    private Sort makeOrderByClause(EventSort sortEvents) {
        switch (sortEvents) {
            case EVENT_DATE:
                return Sort.by("eventDate").descending();
            case VIEWS:
                return Sort.by("views").descending();
            default:
                return Sort.by("publishedOn").descending();
        }
    }
}
