package ru.practicum.main.exception;

public class PageableParametersAreInvalidException extends RuntimeException {
    public PageableParametersAreInvalidException(String message) {
        super(message);
    }
}
