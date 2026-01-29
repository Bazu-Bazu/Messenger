package messenger.user.service.exception.handler;

import dto.response.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import messenger.user.service.exception.UserException;
import messenger.user.service.exception.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException e) {
        log.warn("Validated error: {}", e.getMessage());
        return ResponseEntity.status(400).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUser(UserException e) {
        log.warn("User not found {}", e.getMessage());
        return ResponseEntity.status(500).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception e) {
        log.warn("Unexpected error {}", e.getMessage());
        return ResponseEntity.status(500).body(ErrorResponse.from(e));
    }

}
