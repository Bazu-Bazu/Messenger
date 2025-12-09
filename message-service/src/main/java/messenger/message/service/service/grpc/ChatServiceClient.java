package messenger.message.service.service.grpc;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatServiceClient {

    public void validateUserCanSendMessage(Long chatId, Long senderId) {

    }

    public List<Long> getChatMembersIds(Long chatId) {
        return new ArrayList<>();
    }

    public void validateUserIsChatMember(Long userId) {

    }

}
