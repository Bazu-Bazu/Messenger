package messenger.sso.service.exception.handler;

import dto.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import messenger.sso.service.exception.*;
import messenger.sso.service.exception.mapper.ErrorMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
            SsoUserNotFoundException.class,
            RefreshTokenNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException e) {
        ErrorResponse response = errorMapper.from(e);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleUnauthorized(BadCredentialsException e) {
        String message = "Invalid username or password";

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler({
            IllegalRefreshTokenException.class,
            RefreshTokenExpiredException.class,
            RefreshTokenReuseException.class,
            UserIsNotEnabledException.class
    })
    public ResponseEntity<ErrorResponse> handleUnauthorized(RuntimeException e) {
        ErrorResponse response = errorMapper.from(e);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(AuthorizationException e) {
        ErrorResponse response = errorMapper.from(e);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}
