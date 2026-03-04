package messenger.personal.chat.service.exception.handler;

import dto.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import messenger.personal.chat.service.exception.AuthorizationException;
import messenger.personal.chat.service.exception.UserIsNotActive;
import messenger.personal.chat.service.exception.UserNotFoundException;

import messenger.personal.chat.service.exception.PersonalChatNotFoundException;
import messenger.personal.chat.service.exception.mapper.ErrorMapper;
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

    @ExceptionHandler({
            PersonalChatNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException e) {
        ErrorResponse response = errorMapper.from(e);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserIsNotActive.class)
    public ResponseEntity<ErrorResponse> handleFailedPrecondition(UserIsNotActive e) {
        ErrorResponse response = errorMapper.from(e);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(AuthorizationException e) {
        ErrorResponse response = errorMapper.from(e);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
