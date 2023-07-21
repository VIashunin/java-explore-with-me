package ru.practicum.main.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.stats.dto.RequestDto;
import ru.practicum.main.stats.dto.ResponseDto;
import ru.practicum.main.stats.server.exception.StatsNotFoundException;
import ru.practicum.main.stats.server.exception.TimestampException;
import ru.practicum.main.stats.server.mapper.StatsMapper;
import ru.practicum.main.stats.server.model.Stats;
import ru.practicum.main.stats.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;

    @Override
    public ResponseDto hit(RequestDto requestDto) {
        Stats stats = StatsMapper.mapFromRequestDtoToStats(requestDto);
        stats.setTimestamp(LocalDateTime.now());
        return StatsMapper.mapFromStatsToResponseDto(repository.save(stats));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResponseDto> stats(String start, String end, List<String> uris, boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(start, formatter);
        LocalDateTime endTime = LocalDateTime.parse(end, formatter);
        List<ResponseDto> responseStatsList = new ArrayList<>();
        if (startTime.isAfter(endTime)) {
            throw new TimestampException("Start time is after end time.");
        }
        if (uris != null && !uris.isEmpty()) {
            if (unique) {
                for (String uri : uris) {
                    if (uri.contains("[")) {
                        uri = uri.substring(uri.indexOf('/'), uri.indexOf(']'));
                    }
                    responseStatsList.addAll(repository.findStatsUriUnique(startTime, endTime, uri));
                }
            } else {
                for (String uri : uris) {
                    if (uri.contains("[")) {
                        uri = uri.substring(uri.indexOf('/'), uri.indexOf(']'));
                    }
                    responseStatsList.addAll(repository.findStatsUri(startTime, endTime, uri));
                }
            }
        } else {
            if (unique) {
                responseStatsList.addAll(repository.findStatsUnique(startTime, endTime));
            } else {
                responseStatsList.addAll(repository.findStats(startTime, endTime));
            }
        }
        if (responseStatsList.isEmpty()) {
            throw new StatsNotFoundException("Statistic was not found.");
        } else {
            return responseStatsList.stream()
                    .sorted(Comparator.comparing(ResponseDto::getHits).reversed())
                    .collect(Collectors.toList());
        }
    }
}
