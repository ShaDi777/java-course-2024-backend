package edu.java.bot;

import edu.java.bot.dto.ApiErrorResponse;
import edu.java.bot.exceptions.TooManyRequestsException;
import java.util.Arrays;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
public class BotExceptionHandler {
    @ExceptionHandler({TooManyRequestsException.class})
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ApiErrorResponse handleTooManyRequests(Exception exception, WebRequest request) {
        return createApiErrorResponse(exception, request, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        MethodArgumentTypeMismatchException.class,
        MethodArgumentNotValidException.class,
        WebClientResponseException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleBadRequest(Exception exception, WebRequest request) {
        return createApiErrorResponse(exception, request, HttpStatus.BAD_REQUEST);
    }

    private static ApiErrorResponse createApiErrorResponse(Exception exception, WebRequest request, HttpStatus status) {
        return new ApiErrorResponse(
            request.getDescription(false),
            status.toString(),
            exception.getClass().getName(),
            exception.getMessage(),
            Arrays.stream(exception.getStackTrace()).map(Objects::toString).toArray(String[]::new));
    }
}
