package messenger.sso.service.exception;

public class RefreshTokenReuseException extends RuntimeException {

    public RefreshTokenReuseException(String message) {
        super(message);
    }
}
