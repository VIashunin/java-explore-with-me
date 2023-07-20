package ru.practicum.main.utilities;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.main.exception.PageableParametersAreInvalidException;

public class Paginate {
    public static Pageable paginate(Integer from, Integer size) {
        Pageable page;
        if (from != null && size != null) {
            if (from < 0 || size < 0) {
                throw new PageableParametersAreInvalidException("From or size pageable parameters are invalid.");
            }
            page = PageRequest.of(from > 0 ? from / size : 0, size);
        } else {
            page = PageRequest.of(0, 4);
        }
        return page;
    }
}
