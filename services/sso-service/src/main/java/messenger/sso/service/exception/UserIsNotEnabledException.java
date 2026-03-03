package messenger.sso.service.exception;

public class UserIsNotEnabledException extends RuntimeException {

    public UserIsNotEnabledException(String message) {
        super(message);
    }
}
