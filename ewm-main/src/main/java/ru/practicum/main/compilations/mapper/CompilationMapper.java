package ru.practicum.main.compilations.mapper;

import ru.practicum.main.compilations.dto.CompilationDto;
import ru.practicum.main.compilations.dto.NewCompilationDto;
import ru.practicum.main.compilations.model.Compilation;
import ru.practicum.main.events.model.Event;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.main.events.mapper.EventMapper.mapFromEventListToEventShortDtoList;

public class CompilationMapper {
    public static Compilation mapFromNewCompilationDtoToCompilation(NewCompilationDto newCompilationDto, List<Event> eventList) {
        return Compilation.builder()
                .eventsWithCompilations(eventList)
                .pinned(newCompilationDto.isPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }

    public static CompilationDto mapFromCompilationToCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(mapFromEventListToEventShortDtoList(compilation.getEventsWithCompilations()))
                .pinned(compilation.isPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static List<CompilationDto> mapFromCompilationListToCompilationDtoList(List<Compilation> compilationList) {
        List<CompilationDto> compilationDtoList = new ArrayList<>();
        for (Compilation compilation : compilationList) {
            compilationDtoList.add(mapFromCompilationToCompilationDto(compilation));
        }
        return compilationDtoList;
    }
}