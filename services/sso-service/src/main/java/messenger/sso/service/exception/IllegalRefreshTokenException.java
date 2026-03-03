package messenger.sso.service.exception;

public class IllegalRefreshTokenException extends RuntimeException {

    public IllegalRefreshTokenException(String message) {
        super(message);
    }
}
