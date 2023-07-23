package ru.practicum.main.events.mapper;

import org.springframework.data.domain.Page;
import ru.practicum.main.categories.model.Category;
import ru.practicum.main.events.dto.EventFullDto;
import ru.practicum.main.events.dto.EventShortDto;
import ru.practicum.main.events.dto.NewEventDto;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.model.EventStatus;
import ru.practicum.main.users.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.categories.mapper.CategoryMapper.mapFromCategoryToCategoryDto;
import static ru.practicum.main.users.mapper.UserMapper.mapFromUserToUserShortDto;

public class EventMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event mapFromNewEventDtoToEvent(NewEventDto newEventDto, Category category, User user) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .confirmedRequests(0)
                .createdOn(LocalDateTime.parse(LocalDateTime.now().format(formatter), formatter))
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), formatter))
                .initiator(user)
                .location(newEventDto.getLocation())
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(LocalDateTime.now())
                .requestModeration(newEventDto.isRequestModeration())
                .state(EventStatus.PENDING)
                .title(newEventDto.getTitle())
                .views(0)
                .build();
    }

    public static EventFullDto mapFromEventToEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(mapFromCategoryToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn().toString().replace("T", " "))
                .description(event.getDescription())
                .eventDate(event.getEventDate().toString().replace("T", " "))
                .initiator(mapFromUserToUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn().toString().replace("T", " "))
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static List<EventFullDto> mapFromEventListToEventFullDtoList(List<Event> eventList) {
        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        for (Event event : eventList) {
            eventFullDtoList.add(mapFromEventToEventFullDto(event));
        }

        return eventFullDtoList;
    }

    public static List<EventFullDto> mapFromEventListToEventFullDtoList(Page<Event> eventList) {
        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        for (Event event : eventList) {
            eventFullDtoList.add(mapFromEventToEventFullDto(event));
        }

        return eventFullDtoList;
    }

    public static EventShortDto mapFromEventToEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(mapFromCategoryToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().toString().replace("T", " "))
                .initiator(mapFromUserToUserShortDto(event.getInitiator()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static List<EventShortDto> mapFromEventListToEventShortDtoList(Page<Event> eventList) {
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        for (Event event : eventList) {
            eventShortDtoList.add(mapFromEventToEventShortDto(event));
        }
        return eventShortDtoList;
    }

    public static List<EventShortDto> mapFromEventListToEventShortDtoList(List<Event> eventList) {
        return eventList.stream().map(EventMapper::mapFromEventToEventShortDto).collect(Collectors.toList());
    }
}