package ru.practicum.main.compilations.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.compilations.dto.CompilationDto;
import ru.practicum.main.compilations.dto.NewCompilationDto;
import ru.practicum.main.compilations.dto.UpdateCompilationRequest;
import ru.practicum.main.compilations.model.Compilation;
import ru.practicum.main.compilations.repository.CompilationRepository;
import ru.practicum.main.events.individual.service.IndividualEventService;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.compilations.mapper.CompilationMapper.mapFromCompilationToCompilationDto;
import static ru.practicum.main.compilations.mapper.CompilationMapper.mapFromNewCompilationDtoToCompilation;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final IndividualEventService individualEventService;

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> eventList = new ArrayList<>();
        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            eventList = newCompilationDto.getEvents().stream().map(individualEventService::getEventById).collect(Collectors.toList());
        }
        return mapFromCompilationToCompilationDto(compilationRepository.save(mapFromNewCompilationDtoToCompilation(newCompilationDto, eventList)));
    }

    @Override
    public void deleteCompilation(int compId) {
        compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation is not found or unavailable."));
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto changeCompilation(int compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation previousCompilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation is not found or unavailable."));
        if (updateCompilationRequest.getEvents() != null && !updateCompilationRequest.getEvents().isEmpty()) {
            List<Event> eventList = new ArrayList<>();
            for (Integer eventId : updateCompilationRequest.getEvents()) {
                eventList.add(individualEventService.getEventById(eventId));
            }
            previousCompilation.setEventsWithCompilations(eventList);
        }
        if (updateCompilationRequest.getPinned() != null) {
            previousCompilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null && !updateCompilationRequest.getTitle().isEmpty()) {
            previousCompilation.setTitle(updateCompilationRequest.getTitle());
        }
        return mapFromCompilationToCompilationDto(compilationRepository.save(previousCompilation));
    }
}
