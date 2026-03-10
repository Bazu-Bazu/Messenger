package messenger.message.service.client.grpc;

import lombok.RequiredArgsConstructor;
import messenger.message.service.dto.MemberRightsInPersonalChatDto;
import messenger.message.service.mapper.PersonalChatGrpcMapper;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import personal_chat.PersonalChat;
import personal_chat.PersonalChatServiceGrpc;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PersonalChatGrpcClient {

    @GrpcClient("personal-chat-service")
    private PersonalChatServiceGrpc.PersonalChatServiceBlockingStub blockingStub;

    private final PersonalChatGrpcMapper personalChatGrpcMapper;

    public MemberRightsInPersonalChatDto getMemberRightsInPersonalChat(Long userId, Long chatId) {
        var request = PersonalChat.ValidateUserIsMemberOfPersonalChatRequest.newBuilder()
                .setUserId(userId)
                .setChatId(chatId)
                .build();

        var response = blockingStub.validateUserIsMemberOfPersonalChat(request);
        return personalChatGrpcMapper.fromGrpc(response);
    }

    public Set<Long> getAllPersonalChatMembers(Long chatId) {
        var request = PersonalChat.GetAllPersonalChatMembersRequest.newBuilder()
                .setChatId(chatId)
                .build();

        var response = blockingStub.getAllPersonalChatMembers(request);

        return new HashSet<>(response.getUserIdList());
    }
}
