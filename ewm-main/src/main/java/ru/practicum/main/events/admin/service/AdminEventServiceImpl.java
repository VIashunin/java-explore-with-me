package ru.practicum.main.events.admin.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.categories.admin.service.AdminCategoryService;
import ru.practicum.main.categories.model.Category;
import ru.practicum.main.events.dto.AdminEventRequest;
import ru.practicum.main.events.dto.EventFullDto;
import ru.practicum.main.events.dto.UpdateEventAdminRequest;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.model.EventStatus;
import ru.practicum.main.events.model.QEvent;
import ru.practicum.main.events.repository.EventRepository;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.TimeException;
import ru.practicum.main.locations.model.Location;
import ru.practicum.main.locations.service.LocationService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.main.events.mapper.EventMapper.mapFromEventListToEventFullDtoList;
import static ru.practicum.main.events.mapper.EventMapper.mapFromEventToEventFullDto;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final AdminCategoryService adminCategoryService;
    private final LocationService locationService;

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> findEvents(AdminEventRequest requests) {
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();
        if (requests.hasUsers()) {
            for (Integer id : requests.getUsers()) {
                conditions.add(event.initiator.id.eq(id));
            }
        }
        if (requests.hasStates()) {
            for (Integer id : requests.getCategories()) {
                conditions.add(event.category.id.eq(id));
            }
        }
        if (requests.hasCategories()) {
            for (Integer id : requests.getCategories()) {
                conditions.add(event.category.id.eq(id));
            }
        }
        if (requests.getRangeStart() != null && requests.getRangeEnd() != null) {
            conditions.add(event.eventDate.between(requests.getRangeStart(), requests.getRangeEnd()));
        }
        PageRequest pageRequest = PageRequest.of(requests.getFrom(), requests.getSize());
        Page<Event> eventsPage;
        if (!conditions.isEmpty()) {
            BooleanExpression finalCondition = conditions.stream()
                    .reduce(BooleanExpression::and)
                    .get();
            eventsPage = eventRepository.findAll(finalCondition, pageRequest);
        } else {
            eventsPage = eventRepository.findAll(pageRequest);
        }
        return mapFromEventListToEventFullDtoList(eventsPage);
    }

    @Override
    public EventFullDto changeEvent(int eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event is not found or unavailable."));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = adminCategoryService.findCategoryById(updateEventAdminRequest.getCategory());
            event.setCategory(category);
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getEventDate() != null) {
            LocalDateTime startOldDate = event.getCreatedOn();
            LocalDateTime startNewDate = LocalDateTime.parse(updateEventAdminRequest.getEventDate(), formatter);
            if (Duration.between(startOldDate, startNewDate).toMinutes() < Duration.ofHours(1).toMinutes()) {
                throw new TimeException("Data does not meet the creation rules.");
            }
            event.setEventDate(LocalDateTime.parse(updateEventAdminRequest.getEventDate(), formatter));
        }
        if (updateEventAdminRequest.getLocation() != null) {
            Location location = locationService.save(updateEventAdminRequest.getLocation());
            event.setLocation(location);
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (event.getState().equals(EventStatus.PENDING)) {
            if (updateEventAdminRequest.getStateAction() != null) {
                switch (updateEventAdminRequest.getStateAction()) {
                    case PUBLISH_EVENT:
                        event.setState(EventStatus.PUBLISHED);
                        break;
                    case REJECT_EVENT:
                        event.setState(EventStatus.CANCELED);
                        break;
                }
            }
        } else {
            throw new ConflictException("Data integrity violation has occurred.");
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        return mapFromEventToEventFullDto(eventRepository.save(event));
    }
}
