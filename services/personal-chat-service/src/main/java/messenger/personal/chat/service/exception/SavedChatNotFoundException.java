package messenger.personal.chat.service.exception;

public class SavedChatNotFoundException extends RuntimeException {

    public SavedChatNotFoundException(String message) {
        super(message);
    }
}
