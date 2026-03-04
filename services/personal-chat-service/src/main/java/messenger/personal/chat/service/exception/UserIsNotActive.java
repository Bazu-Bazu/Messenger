package messenger.personal.chat.service.exception;

public class UserIsNotActive extends RuntimeException {

    public UserIsNotActive(String message) {
        super(message);
    }
}
