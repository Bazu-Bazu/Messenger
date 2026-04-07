package messenger.chat.service.service;

import enums.ChatMemberPermissions;
import enums.ChatType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import messenger.chat.service.domain.entity.Chat;
import messenger.chat.service.domain.entity.User;
import messenger.chat.service.domain.entity.UserChat;
import messenger.chat.service.domain.repository.UserChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserChatService {

    private final UserChatRepository userChatRepository;
    private final UserService userService;

    @Transactional
    public void createUsersChat(Chat chat, List<Long> userIds, ChatMemberPermissions permissions) {
        List<User> users = userService.getUsersByIds(userIds);

        if (users.size() != userIds.size()) {
            log.warn("Some users not found. Requested = {}, found = {}", userIds, users.stream().map(User::getUserId));
            return;
        }

        List<UserChat> userChats = users.stream()
                .map(user -> UserChat.builder()
                                   .user(user)
                                   .chat(chat)
                                   .permissions(permissions)
                                   .build())
                .toList();

        userChatRepository.saveAll(userChats);
    }

    @Transactional
    public void changeRoles(Long chatId, List<Long> userIds, ChatMemberPermissions permissions) {
        int updated = userChatRepository.changePermissions(userIds, permissions, chatId, ChatType.GROUP);

        if (updated == 0) {
            log.warn("Roles not changed (maybe already set) for {} chat {} users {}", ChatType.GROUP, chatId, userIds);
        }
    }

    @Transactional
    public void deleteUsersChat(Long chatId, List<Long> userIds) {
        int updated = userChatRepository.deleteUsersChats(userIds, chatId, ChatType.GROUP);

        if (updated == 0) {
            log.warn("Users already removed or not found for {} chat {} users {}", ChatType.GROUP, chatId, userIds);
        }
    }
}
