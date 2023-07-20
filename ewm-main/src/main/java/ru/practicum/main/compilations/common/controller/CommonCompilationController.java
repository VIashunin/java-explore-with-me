package ru.practicum.main.compilations.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.compilations.common.service.CommonCompilationService;
import ru.practicum.main.compilations.dto.CompilationDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommonCompilationController {
    private final CommonCompilationService commonCompilationService;

    @GetMapping(path = "/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) String pinned,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return commonCompilationService.getCompilations(pinned, from, size);
    }

    @GetMapping(path = "/compilations/{compId}")
    public CompilationDto getCompilationById(@PathVariable int compId) {
        return commonCompilationService.getCompilationById(compId);
    }
}
