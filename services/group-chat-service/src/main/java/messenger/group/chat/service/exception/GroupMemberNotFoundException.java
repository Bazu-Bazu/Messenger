package messenger.group.chat.service.exception;

public class GroupMemberNotFoundException extends RuntimeException {

    public GroupMemberNotFoundException(String message) {
        super(message);
    }
}
