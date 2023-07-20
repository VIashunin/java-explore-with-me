package ru.practicum.main.compilations.common.service;

import ru.practicum.main.compilations.dto.CompilationDto;

import java.util.List;

public interface CommonCompilationService {
    List<CompilationDto> getCompilations(String pinned, int from, int size);

    CompilationDto getCompilationById(int compId);
}