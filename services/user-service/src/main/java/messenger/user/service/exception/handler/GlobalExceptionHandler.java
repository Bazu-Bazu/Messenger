package messenger.user.service.exception.handler;

import dto.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import messenger.user.service.exception.UserNotFoundException;
import messenger.user.service.exception.mapper.ErrorMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorMapper errorMapper;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorResponse response = ErrorResponse.builder()
                .errorCode(400)
                .error("ValidationError")
                .message(message)
                .timestamp(Instant.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleNotFound(UserNotFoundException e) {
        ErrorResponse response = errorMapper.from(e);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}
