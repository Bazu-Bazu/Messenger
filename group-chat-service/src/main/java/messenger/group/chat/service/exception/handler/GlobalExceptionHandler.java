package messenger.group.chat.service.exception.handler;

import dto.response.ErrorResponse;
import exception.UserIsNotActive;
import exception.UserNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e) {
        log.warn("User not found. Error: {}", e.getMessage());
        return ResponseEntity.status(404).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(UserIsNotActive.class)
    public ResponseEntity<ErrorResponse> handleUserIsNotActive(UserIsNotActive e) {
        log.warn("User is not active. Error: {}", e.getMessage());
        return ResponseEntity.status(409).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception e) {
        log.warn("Unexpected error {}", e.getMessage());
        return ResponseEntity.status(500).body(ErrorResponse.from(e));
    }

}
