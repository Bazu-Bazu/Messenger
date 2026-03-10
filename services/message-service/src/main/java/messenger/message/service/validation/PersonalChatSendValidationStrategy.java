package messenger.message.service.validation;

import enums.ChatType;
import lombok.RequiredArgsConstructor;
import messenger.message.service.client.grpc.PersonalChatGrpcClient;
import messenger.message.service.dto.MemberRightsInPersonalChatDto;
import messenger.message.service.exception.AuthorizationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonalChatSendValidationStrategy implements ChatSendValidationStrategy {

    private final PersonalChatGrpcClient personalChatServiceClient;

    @Override
    public ChatType getSupportedType() {
        return ChatType.PERSONAL;
    }

    @Override
    public void validateSending(Long userId, Long chatId) {
        MemberRightsInPersonalChatDto rights = personalChatServiceClient.getMemberRightsInPersonalChat(userId, chatId);

        if (!rights.isMember()) {
            throw new AuthorizationException(
                    String.format("User %d cannot send messages in personal chat %d", userId, chatId)
            );
        }
    }

    @Override
    public void validateReading(Long userId, Long chatId) {
        MemberRightsInPersonalChatDto rights = personalChatServiceClient.getMemberRightsInPersonalChat(userId, chatId);

        if (!rights.isMember()) {
            throw new AuthorizationException(
                    String.format("User %d cannot read messages in personal chat %d", userId, chatId)
            );
        }
    }
}
