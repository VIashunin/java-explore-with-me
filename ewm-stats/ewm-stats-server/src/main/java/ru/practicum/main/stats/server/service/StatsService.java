package ru.practicum.main.stats.server.service;

import ru.practicum.main.stats.dto.RequestDto;
import ru.practicum.main.stats.dto.ResponseDto;

import java.util.List;

public interface StatsService {
    ResponseDto hit(RequestDto requestDto);

    List<ResponseDto> stats(String start, String end, List<String> uri, boolean unique);
}
