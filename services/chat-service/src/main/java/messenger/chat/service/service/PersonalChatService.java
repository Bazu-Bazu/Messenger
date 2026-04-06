package messenger.chat.service.service;

import dto.event.PersonalChatEvent;
import enums.ChatMemberRole;
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
public class PersonalChatService {

    private final ChatRepository chatRepository;
    private final UserChatService userChatService;

    @Transactional
    public void create(PersonalChatEvent event) {
        Chat chat = Chat.builder()
                .chatId(event.id())
                .chatType(ChatType.PERSONAL)
                .build();

        Chat savedChat = chatRepository.save(chat);

        userChatService.createUsersChat(savedChat, List.of(event.user1Id(), event.user2Id()), ChatMemberRole.ADMIN);
    }

    @Transactional
    public void delete(PersonalChatEvent event) {
        int updated = chatRepository.deleteByChatIdAndChatType(event.id(), ChatType.PERSONAL);

        if (updated == 0) {
            log.warn("{} chat {} deleted or not found", ChatType.PERSONAL, event.id());
        }
    }
}
