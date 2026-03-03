package messenger.sso.service.exception;

public class UnknowEventType extends RuntimeException {

    public UnknowEventType(String message) {
        super(message);
    }
}
