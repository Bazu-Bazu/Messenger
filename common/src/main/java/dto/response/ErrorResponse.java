package dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ErrorResponse {

    private Integer errorCode;
    private String error;
    private String message;
    private Instant timestamp;

    public static ErrorResponse from(Exception e) {
        return ErrorResponse.builder()
                .error(e.getClass().getSimpleName())
                .message(e.getMessage())
                .timestamp(Instant.now())
                .build();
    }

}
