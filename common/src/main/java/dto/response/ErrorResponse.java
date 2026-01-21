package dto.response;

import exception.AuthorizationException;
import exception.MessageException;
import exception.UserIsNotActive;
import exception.UserNotFoundException;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.concurrent.CompletionException;

@Data
@Builder
public class ErrorResponse {

    private Integer errorCode;
    private String error;
    private String message;
    private Instant timestamp;

    public static ErrorResponse from(Exception e) {
        Throwable cause = unwrapCompletionException(e);

        return ErrorResponse.builder()
                .errorCode(getErrorCode(cause))
                .error(cause.getClass().getSimpleName())
                .message(cause.getMessage())
                .timestamp(Instant.now())
                .build();
    }

    private static Throwable unwrapCompletionException(Throwable e) {
        Throwable cause = e;
        while (cause instanceof CompletionException) {
            cause = cause.getCause();
            if (cause == null) return e;
        }
        return cause;
    }

    private static int getErrorCode(Throwable e) {
        if (e instanceof AuthorizationException) return 403;
        if (e instanceof UserIsNotActive) return 409;
        if (e instanceof MessageException) return 404;
        if (e instanceof UserNotFoundException) return 404;
        return 500;
    }

}
