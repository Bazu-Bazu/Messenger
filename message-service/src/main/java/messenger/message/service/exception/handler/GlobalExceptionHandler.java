package messenger.message.service.exception.handler;

import dto.response.ErrorResponse;
import exception.AuthorizationException;
import exception.UserIsNotActive;
import exception.UserNotFoundException;
import lombok.extern.log4j.Log4j2;
import messenger.message.service.exception.MessageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.CompletionException;

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

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<ErrorResponse> handleGroupChatNotFound(MessageException e) {
        log.warn("Message not found. Error: {}", e.getMessage());
        return ResponseEntity.status(404).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorization(AuthorizationException e) {
        log.warn("The user doesn't have enough rights. Error: {}", e.getMessage());
        return ResponseEntity.status(403).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception e) {
        log.warn("Unexpected error {}", e.getMessage());
        return ResponseEntity.status(500).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(CompletionException.class)
    public ResponseEntity<ErrorResponse> handleCompletionException(CompletionException e) {
        Throwable cause = e.getCause();
        if (cause instanceof AuthorizationException) {
            return handleAuthorization((AuthorizationException) cause);
        }
        if (cause instanceof UserNotFoundException) {
            return handleUserNotFound((UserNotFoundException) cause);
        }
        if (cause instanceof UserIsNotActive) {
            return handleUserIsNotActive((UserIsNotActive) cause);
        }
        if (cause instanceof MessageException) {
            return handleGroupChatNotFound((MessageException) cause);
        }

        return handleUnknown(e);
    }

}
