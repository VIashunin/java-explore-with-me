package ru.practicum.main.events.individual.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.categories.common.service.CommonCategoryService;
import ru.practicum.main.categories.model.Category;
import ru.practicum.main.events.dto.EventFullDto;
import ru.practicum.main.events.dto.EventShortDto;
import ru.practicum.main.events.dto.NewEventDto;
import ru.practicum.main.events.dto.UpdateEventUserRequest;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.model.EventStatus;
import ru.practicum.main.events.repository.EventRepository;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.TimeException;
import ru.practicum.main.locations.service.LocationService;
import ru.practicum.main.users.admin.service.AdminUserService;
import ru.practicum.main.users.model.User;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.main.categories.mapper.CategoryMapper.mapFromCategoryDtoToCategory;
import static ru.practicum.main.events.mapper.EventMapper.*;
import static ru.practicum.main.utilities.Paginate.paginate;

@Service
@Transactional
@RequiredArgsConstructor
public class IndividualEventServiceImpl implements IndividualEventService {
    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final AdminUserService adminUserService;
    private final CommonCategoryService commonCategoryService;

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getEventsByUser(int userId, int from, int size) {
        Pageable page = paginate(from, size);
        return mapFromEventListToEventShortDtoList(eventRepository.findEventsByInitiator_Id(userId, page));
    }

    @Override
    public EventFullDto createEvent(int userId, NewEventDto newEventDto) {
        validTime(newEventDto.getEventDate());
        User user = adminUserService.getUserById(userId);
        Category category = commonCategoryService.findCategoryById(newEventDto.getCategory());
        locationService.save(newEventDto.getLocation());
        return mapFromEventToEventFullDto(eventRepository.save(mapFromNewEventDtoToEvent(newEventDto, category, user)));
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getEventByUserFullInfo(int userId, int eventId) {
        return mapFromEventToEventFullDto(eventRepository.findEventByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event is not found or unavailable.")));
    }

    @Override
    public EventFullDto changeEventByUser(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event is not found or unavailable."));
        if (event.getState().equals(EventStatus.PENDING) || event.getState().equals(EventStatus.CANCELED)) {
            if (updateEventUserRequest.getEventDate() != null) {
                validTime(updateEventUserRequest.getEventDate());
                event.setEventDate(LocalDateTime.parse(updateEventUserRequest.getEventDate()));
            }
            if (updateEventUserRequest.getAnnotation() != null) {
                event.setAnnotation(updateEventUserRequest.getAnnotation());
            }
            if (updateEventUserRequest.getCategory() != null) {
                event.setCategory(mapFromCategoryDtoToCategory(updateEventUserRequest.getCategory()));
            }
            if (updateEventUserRequest.getDescription() != null) {
                event.setDescription(updateEventUserRequest.getDescription());
            }
            if (updateEventUserRequest.getLocation() != null) {
                event.setLocation(updateEventUserRequest.getLocation());
            }
            if (updateEventUserRequest.getPaid() != null) {
                event.setPaid(updateEventUserRequest.getPaid());
            }
            if (updateEventUserRequest.getParticipantLimit() != null) {
                event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
            }
            if (updateEventUserRequest.getRequestModeration() != null) {
                event.setRequestModeration(updateEventUserRequest.getRequestModeration());
            }
            if (updateEventUserRequest.getStateAction() != null) {
                switch (updateEventUserRequest.getStateAction()) {
                    case SEND_TO_REVIEW:
                        event.setState(EventStatus.PENDING);
                        break;
                    case CANCEL_REVIEW:
                        event.setState(EventStatus.CANCELED);
                        break;
                }
            }
            if (updateEventUserRequest.getTitle() != null) {
                event.setTitle(updateEventUserRequest.getTitle());
            }
            return mapFromEventToEventFullDto(eventRepository.save(event));
        } else {
            throw new ConflictException("Data integrity violation has occurred.");
        }
    }

    @Override
    public Event getEventById(int eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event is not found or unavailable."));
    }

    private void validTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(time, formatter);
        if (Duration.between(LocalDateTime.now(), startDate).toMinutes() < Duration.ofHours(2).toMinutes()) {
            throw new TimeException("Data does not meet the creation rules.");
        }
    }
}
