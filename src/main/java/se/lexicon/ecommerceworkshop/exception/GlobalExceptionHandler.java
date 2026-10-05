package se.lexicon.ecommerceworkshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException exception) {
        return buildError(HttpStatus.NOT_FOUND, exception.getMessage(), Map.of());
    }

    @ExceptionHandler({EmailAlreadyExistsException.class, CategoryAlreadyExistsException.class})
    public ResponseEntity<ApiError> handleConflict(RuntimeException exception) {
        return buildError(HttpStatus.CONFLICT, exception.getMessage(), Map.of());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException exception) {
        return buildError(HttpStatus.BAD_REQUEST, exception.getMessage(), Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage()));

        return buildError(HttpStatus.BAD_REQUEST, "Validation failed", fieldErrors);
    }

    private ResponseEntity<ApiError> buildError(
            HttpStatus status,
            String message,
            Map<String, String> fieldErrors) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                status.value(),
                message,
                fieldErrors);

        return ResponseEntity.status(status).body(error);
    }
}
