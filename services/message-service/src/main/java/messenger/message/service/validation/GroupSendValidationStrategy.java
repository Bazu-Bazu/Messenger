package messenger.message.service.validation;

import enums.ChatType;
import lombok.RequiredArgsConstructor;
import messenger.message.service.client.grpc.GroupGrpcClient;
import messenger.message.service.dto.MemberRightsInGroupDto;
import messenger.message.service.exception.AuthorizationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupSendValidationStrategy implements ChatSendValidationStrategy {

    private final GroupGrpcClient groupChatServiceClient;

    @Override
    public ChatType getSupportedType() {
        return ChatType.GROUP;
    }

    @Override
    public void validateSending(Long userId, Long chatId) {
        MemberRightsInGroupDto rights = groupChatServiceClient.getMemberRightsInGroup(userId, chatId);

        if (!rights.canSend()) {
            throw new AuthorizationException(
                    String.format("User %d cannot send messages in group %d", userId, chatId)
            );
        }
    }

    @Override
    public void validateReading(Long userId, Long chatId) {
        MemberRightsInGroupDto rights = groupChatServiceClient.getMemberRightsInGroup(userId, chatId);

        if (!rights.canRead()) {
            throw new AuthorizationException(
                    String.format("User %d cannot read messages in group %d", userId, chatId)
            );
        }
    }
}
