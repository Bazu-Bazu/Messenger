package messenger.message.service.exception;

public class IllegalPayloadTypeException extends RuntimeException {

    public IllegalPayloadTypeException(String message) {
        super(message);
    }
}
