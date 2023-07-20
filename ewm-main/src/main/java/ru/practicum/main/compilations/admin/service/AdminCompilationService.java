package ru.practicum.main.compilations.admin.service;

import ru.practicum.main.compilations.dto.CompilationDto;
import ru.practicum.main.compilations.dto.NewCompilationDto;
import ru.practicum.main.compilations.dto.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(int compId);

    CompilationDto changeCompilation(int compId, UpdateCompilationRequest updateCompilationRequest);
}
