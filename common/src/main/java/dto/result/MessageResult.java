package dto.result;

import dto.response.ErrorResponse;
import dto.response.MessageResponse;

public record MessageResult(

        MessageResponse success,
        ErrorResponse error,
        boolean isSuccess
) {

    public static MessageResult success(MessageResponse response) {
        return new MessageResult(response, null, true);
    }

    public static MessageResult error(ErrorResponse response) {
        return new MessageResult(null, response, false);
    }
}
