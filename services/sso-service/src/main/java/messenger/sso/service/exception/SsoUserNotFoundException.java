package messenger.sso.service.exception;

public class SsoUserNotFoundException extends RuntimeException {

    public SsoUserNotFoundException(String message) {
        super(message);
    }
}
