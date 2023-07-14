package ru.practicum.main.stats.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.stats.dto.RequestDto;
import ru.practicum.main.stats.dto.ResponseDto;
import ru.practicum.main.stats.server.service.StatsService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
public class StatsController {
    private final StatsService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/hit")
    public ResponseDto hit(@Valid @RequestBody RequestDto requestDto) {
        return service.hit(requestDto);
    }

    @GetMapping(path = "/stats")
    public List<ResponseDto> stats(@RequestParam String start,
                                   @RequestParam String end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(defaultValue = "false") boolean unique) {
        return service.stats(start, end, uris, unique);
    }
}

