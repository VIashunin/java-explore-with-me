package ru.practicum.main.compilations.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.compilations.dto.CompilationDto;
import ru.practicum.main.compilations.repository.CompilationRepository;
import ru.practicum.main.exception.NotFoundException;

import java.util.List;

import static ru.practicum.main.compilations.mapper.CompilationMapper.mapFromCompilationListToCompilationDtoList;
import static ru.practicum.main.compilations.mapper.CompilationMapper.mapFromCompilationToCompilationDto;
import static ru.practicum.main.utilities.Paginate.paginate;

@Service
@Transactional
@RequiredArgsConstructor
public class CommonCompilationServiceImpl implements CommonCompilationService {
    private final CompilationRepository compilationRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDto> getCompilations(String pinned, int from, int size) {
        Pageable page = paginate(from, size);
        return mapFromCompilationListToCompilationDtoList(compilationRepository.findCompilationsByPinnedIs(Boolean.parseBoolean(pinned), page));
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto getCompilationById(int compId) {
        return mapFromCompilationToCompilationDto(compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation is not found or unavailable.")));
    }
}
