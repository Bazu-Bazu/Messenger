package messenger.message.service.service;

import lombok.RequiredArgsConstructor;
import messenger.message.service.client.grpc.GroupGrpcClient;
import messenger.message.service.client.grpc.PersonalChatGrpcClient;
import messenger.message.service.kafka.producer.MessageEventProducer;
import messenger.message.service.domain.entity.Message;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class NotifyService {

    private final GroupGrpcClient groupChatServiceClient;
    private final PersonalChatGrpcClient personalChatServiceClient;
    private final MessageEventProducer messageEventProducer;

    @Async
    public void notifyChatMembers(Message message) {
        Set<Long> memberIds = switch (message.getChatType()) {
            case PERSONAL -> personalChatServiceClient.getAllPersonalChatMembers(message.getChatId());
            case GROUP -> groupChatServiceClient.getAllGroupChatMembers(message.getChatId());
        };

        memberIds.remove(message.getSenderId());

        messageEventProducer.publishMessageNotification(message, memberIds);
    }
}
