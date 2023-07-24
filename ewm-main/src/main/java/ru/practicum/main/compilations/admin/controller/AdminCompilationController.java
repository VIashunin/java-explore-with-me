package ru.practicum.main.compilations.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.compilations.admin.service.AdminCompilationService;
import ru.practicum.main.compilations.dto.CompilationDto;
import ru.practicum.main.compilations.dto.NewCompilationDto;
import ru.practicum.main.compilations.dto.UpdateCompilationRequest;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class AdminCompilationController {
    private final AdminCompilationService adminCompilationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/compilations")
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return adminCompilationService.createCompilation(newCompilationDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/compilations/{compId}")
    public void deleteCompilation(@PathVariable int compId) {
        adminCompilationService.deleteCompilation(compId);
    }

    @PatchMapping(path = "/compilations/{compId}")
    public CompilationDto changeCompilation(@PathVariable int compId,
                                            @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        return adminCompilationService.changeCompilation(compId, updateCompilationRequest);
    }
}
