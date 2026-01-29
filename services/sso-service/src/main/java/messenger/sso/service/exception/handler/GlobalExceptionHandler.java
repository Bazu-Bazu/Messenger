package messenger.sso.service.exception.handler;

import dto.response.ErrorResponse;
import exception.AuthorizationException;
import lombok.extern.log4j.Log4j2;
import messenger.sso.service.exception.RefreshTokenException;
import messenger.sso.service.exception.SsoUserException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ErrorResponse> handleRefreshToken(RefreshTokenException e) {
        log.warn("Refresh token not found: {}", e.getMessage());
        return ResponseEntity.status(404).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(SsoUserException.class)
    public ResponseEntity<ErrorResponse> handleSsoUser(SsoUserException e) {
        log.warn("Sso user not found: {}", e.getMessage());
        return ResponseEntity.status(404).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorization(AuthorizationException e) {
        log.warn("The user doesn't have enough rights. Error: {}", e.getMessage());
        return ResponseEntity.status(403).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException e) {
        log.warn("The user provided incorrect credentials");
        return ResponseEntity.status(401).body(ErrorResponse.from(e));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception e) {
        log.warn("Unexpected error {}", e.getMessage());
        return ResponseEntity.status(500).body(ErrorResponse.from(e));
    }

}
