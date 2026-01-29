package messenger.user.service.dto.request;

public record CreateUserRequest(
        String username,
        String phone,
        String password
) {}
