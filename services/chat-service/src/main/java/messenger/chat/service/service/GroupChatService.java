package messenger.chat.service.service;

import dto.event.GroupChatEvent;
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
public class GroupChatService {

    private final ChatRepository chatRepository;
    private final UserChatService userChatService;
    private final ChatService chatService;

    @Transactional
    public void create(GroupChatEvent event) {
        Chat chat = Chat.builder()
                .chatId(event.id())
                .chatType(ChatType.GROUP)
                .title(event.name())
                .avatarId(event.avatarId())
                .build();

        Chat savedChat = chatRepository.save(chat);

        userChatService.createUsersChat(savedChat, List.of(event.ownerId()), ChatMemberRole.OWNER);

        if (event.members() != null) {
            userChatService.createUsersChat(savedChat, event.members().userIds(), event.members().role());
        }
    }

    @Transactional
    public void addMembers(GroupChatEvent event) {
        Chat chat = chatService.findChatByIdAndType(event.id(), ChatType.GROUP);

        if (event.members() != null) {
            userChatService.createUsersChat(chat, event.members().userIds(), event.members().role());
        } else {
            log.warn("Attempt add members without members in {} chat {}", ChatType.GROUP, event.id());
        }
    }

    @Transactional
    public void changeRoles(GroupChatEvent event) {
        if (event.members() != null) {
            userChatService.changeRoles(event.id(), event.members().userIds(), event.members().role());
        } else {
            log.warn("Attempt change roles without members in {} chat {}", ChatType.GROUP, event.id());
        }
    }

    @Transactional
    public void removeMembers(GroupChatEvent event) {
        if (event.members() != null) {
            userChatService.deleteUsersChat(event.id(), event.members().userIds());
        } else {
            log.warn("Attempt remove members without members in {} chat {}", ChatType.GROUP, event.id());
        }
    }

    @Transactional
    public void changeInfo(GroupChatEvent event) {
        int updated = chatRepository.changeInfoForGroup(event.id(), ChatType.GROUP, event.name(), event.avatarId());

        if (updated == 0) {
            log.warn("{} chat {} info not changed", ChatType.GROUP, event.id());
        }
    }

    @Transactional
    public void delete(GroupChatEvent event) {
        int updated = chatRepository.deleteByChatIdAndChatType(event.id(), ChatType.GROUP);

        if (updated == 0) {
            log.warn("{} chat {} deleted or not found", ChatType.PERSONAL, event.id());
        }
    }
}
