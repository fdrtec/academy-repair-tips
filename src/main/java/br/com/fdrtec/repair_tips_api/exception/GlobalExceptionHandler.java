package br.com.fdrtec.repair_tips_api.exception;

import br.com.fdrtec.repair_tips_api.service.ResourceNotFoundException;
import java.net.URI;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(ResourceNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle("Not Found");
        problemDetail.setType(URI.create("https://http.dev/problems/not-found"));
        problemDetail.setProperty("timestamp", Instant.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(MethodArgumentNotValidException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problemDetail.setTitle("Validation failed");
        problemDetail.setType(URI.create("https://http.dev/problems/validation-error"));
        problemDetail.setProperty("timestamp", Instant.now());

        List<Map<String, String>> errors = exception.getBindingResult().getFieldErrors().stream()
            .map(this::toErrorDetail)
            .toList();

        problemDetail.setProperty("errors", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpected(Exception exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setType(URI.create("https://http.dev/problems/internal-error"));
        problemDetail.setProperty("timestamp", Instant.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }

    private Map<String, String> toErrorDetail(FieldError fieldError) {
        Map<String, String> detail = new LinkedHashMap<>();
        detail.put("field", fieldError.getField());
        detail.put("message", fieldError.getDefaultMessage());
        return detail;
    }
}
