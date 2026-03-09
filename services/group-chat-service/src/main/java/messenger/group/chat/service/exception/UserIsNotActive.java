package messenger.group.chat.service.exception;

public class UserIsNotActive extends RuntimeException {

    public UserIsNotActive(String message) {
        super(message);
    }
}
