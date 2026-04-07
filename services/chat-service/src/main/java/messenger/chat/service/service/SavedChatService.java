package messenger.chat.service.service;

import dto.event.SavedChatEvent;
import enums.ChatMemberPermissions;
import enums.ChatType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import messenger.chat.service.domain.entity.Chat;
import messenger.chat.service.domain.repository.ChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class SavedChatService {

    private final ChatRepository chatRepository;
    private final UserChatService userChatService;

    @Transactional
    public void create(SavedChatEvent event) {
        Chat chat = Chat.builder()
                .chatId(event.id())
                .chatType(ChatType.SAVED)
                .title("Saved")
                .build();

        Chat savedChat = chatRepository.save(chat);

        userChatService.createUsersChat(savedChat, List.of(event.userId()), ChatMemberPermissions.ALL);
    }

    @Transactional
    public void delete(SavedChatEvent event) {
        int updated = chatRepository.deleteByChatIdAndChatType(event.id(), ChatType.SAVED);

        if (updated == 0) {
            log.warn("{} chat {} already deleted or not found", ChatType.SAVED, event.id());
        }
    }
}
