package messenger.message.service.validation;

import enums.ChatType;
import messenger.message.service.exception.IllegalChatTypeException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ChatValidationFactory {

    private final Map<ChatType, ChatSendValidationStrategy> strategies;

    public ChatValidationFactory(List<ChatSendValidationStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(ChatSendValidationStrategy::getSupportedType, s -> s));
    }

    public ChatSendValidationStrategy getStrategy(ChatType chatType) {
        return Optional.ofNullable(strategies.get(chatType))
                .orElseThrow(() -> new IllegalChatTypeException("Unsupported chat type: " + chatType));
    }
}
