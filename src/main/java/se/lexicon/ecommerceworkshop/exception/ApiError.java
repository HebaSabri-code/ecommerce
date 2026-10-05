package se.lexicon.ecommerceworkshop.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiError(
        LocalDateTime timestamp,
        int status,
        String message,
        Map<String, String> fieldErrors
) {
}
