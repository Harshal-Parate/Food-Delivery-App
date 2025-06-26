package com.example.Zomato.ZomatoApplication.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(NoSuchElementException exception) {
        ApiError apiError = ApiError
                .builder()
                .setMessage(exception.getMessage())
                .setStatus(HttpStatus.BAD_REQUEST)
                .build();
        return handleApiError(apiError);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(Exception exception) {
        ApiError apiError = ApiError
                .builder()
                .setMessage(exception.getMessage())
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return handleApiError(apiError);
    }

    @ExceptionHandler
    public ResponseEntity<ApiResponse<?>> handleInvalidInputs(MethodArgumentNotValidException exception) {
        List<String> subErrors = exception.getAllErrors()
                .stream().map(e -> e.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError error = ApiError
                .builder()
                .setMessage(exception.getMessage())
                .setSubErrors(subErrors)
                .setStatus(HttpStatus.BAD_REQUEST)
                .build();
        return handleApiError(error);
    }


    private ResponseEntity<ApiResponse<?>> handleApiError(ApiError error) {
        return new ResponseEntity<>(new ApiResponse<>(error), error.getStatus());
    }
}
