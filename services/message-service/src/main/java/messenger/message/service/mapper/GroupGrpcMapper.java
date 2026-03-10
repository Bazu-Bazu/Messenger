package messenger.message.service.mapper;

import group_chat.GroupChat;
import messenger.message.service.dto.MemberRightsInGroupDto;
import org.springframework.stereotype.Component;

@Component
public class GroupGrpcMapper {

    public MemberRightsInGroupDto fromGrpc(GroupChat.ValidateMemberRightsInGroupChatResponse response) {
        return MemberRightsInGroupDto.builder()
                .userId(response.getUserId())
                .chatId(response.getChatId())
                .canSend(response.getCanSendMessage())
                .canRead(response.getCanGetMessage())
                .build();
    }
}
