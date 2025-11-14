package messenger.user.service.exception;

import java.util.List;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, List<String> errors) {
        super(message + errorsToString(errors));
    }

    private static String errorsToString(List<String> errors) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < errors.size(); i++) {
            stringBuilder.append(i + 1)
                         .append(") ")
                         .append(errors)
                         .append(".");
        }

        return stringBuilder.toString();
    }

}
