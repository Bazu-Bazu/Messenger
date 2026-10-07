package com.burkina.messenger.service;

import com.burkina.common.dto.event.messenger.groupChat.common.GroupChatMemberInfo;
import com.burkina.common.enums.messenger.ChatType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.burkina.messenger.domain.entity.Chat;
import com.burkina.messenger.domain.entity.User;
import com.burkina.messenger.domain.entity.UserChat;
import com.burkina.messenger.domain.repository.UserChatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserChatService {

    private final UserChatRepository userChatRepository;
    private final UserService userService;

    @Transactional
    public void createUsersChat(Chat chat, List<Long> userIds) {
        List<User> users = userService.getUsersByIds(userIds);

        if (users.size() != userIds.size()) {
            log.warn("Some users not found. Requested = {}, found = {}", userIds, users.stream().map(User::getUserId));
            return;
        }

        List<UserChat> userChats = users.stream()
                .map(user -> UserChat.builder()
                                   .user(user)
                                   .chat(chat)
                                   .build())
                .toList();

        userChatRepository.saveAll(userChats);
    }

    @Transactional
    public void changeRoles(Long chatId, List<GroupChatMemberInfo> members) {
        List<Long> userIds = members.stream().map(GroupChatMemberInfo::userId).toList();
        List<UserChat> userChats = getUsersByIdsAndGroupChat(chatId, userIds);

        Map<Long, Boolean> memberMap = members.stream()
                .collect(Collectors.toMap(
                        GroupChatMemberInfo::userId,
                        GroupChatMemberInfo::canSendMessage
                ));

        userChats.forEach(userChat -> userChat.setCanSend(memberMap.get(userChat.getUser().getUserId())));
    }

    @Transactional(readOnly = true)
    public List<UserChat> getUsersByIdsAndGroupChat(Long chatId, List<Long> userIds) {
        return userChatRepository.findByUserIdsAndGroupChat(userIds, chatId);
    }

    @Transactional
    public void deleteUsersChat(Long chatId, List<Long> userIds) {
        int updated = userChatRepository.deleteUsersChats(userIds, chatId, ChatType.GROUP);

        if (updated == 0) {
            log.warn("Users already removed or not found for {} chat {} users {}", ChatType.GROUP, chatId, userIds);
        }
    }
}
