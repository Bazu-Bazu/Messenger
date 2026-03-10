package messenger.message.service.mapper;

import messenger.message.service.dto.MemberRightsInPersonalChatDto;
import org.springframework.stereotype.Component;
import personal_chat.PersonalChat;

@Component
public class PersonalChatGrpcMapper {

    public MemberRightsInPersonalChatDto fromGrpc(PersonalChat.ValidateUserIsMemberOfPersonalChatResponse response) {
        return MemberRightsInPersonalChatDto.builder()
                .userId(response.getUserId())
                .chatId(response.getChatId())
                .isMember(response.getIsMember())
                .build();
    }
}
