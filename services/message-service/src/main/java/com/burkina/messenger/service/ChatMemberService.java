package com.burkina.messenger.service;

import com.burkina.common.dto.event.messenger.groupChat.*;
import com.burkina.common.dto.event.messenger.groupChat.common.GroupChatMemberInfo;
import com.burkina.common.dto.event.messenger.personalChat.PersonalChatCreatedEvent;
import com.burkina.common.dto.event.messenger.personalChat.PersonalChatDeletedEvent;
import com.burkina.common.dto.event.messenger.savedChat.SavedChatCreatedEvent;
import com.burkina.common.dto.event.messenger.savedChat.SavedChatDeletedEvent;
import com.burkina.common.enums.messenger.ChatType;
import com.burkina.messenger.domain.entity.ChatMember;
import com.burkina.messenger.domain.repository.ChatMemberRepository;
import com.burkina.messenger.exception.ChatMemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMemberService {

    private final ChatMemberRepository memberRepository;
    private final CacheInvalidationService cacheInvalidationService;

    private final static String CHAT_MEMBER = "chatMember";
    private final static String CHAT_MEMBERS = "chatMembers";

    @Transactional
    public void createMembers(SavedChatCreatedEvent event) {
        ChatMember member = createMember(event.userId(), event.chatId(), ChatType.SAVED);

        memberRepository.save(member);
    }

    @Transactional
    public void createMembers(PersonalChatCreatedEvent event) {
        ChatMember member1 = createMember(event.user1Id(), event.chatId(), ChatType.PERSONAL);
        ChatMember member2 = createMember(event.user2Id(), event.chatId(), ChatType.PERSONAL);

        memberRepository.saveAll(List.of(member1, member2));
    }

    @Transactional
    public void createMembers(GroupChatCreatedEvent event) {
        if (event.userIds().isEmpty()) return;

        List<ChatMember> members = event.userIds().stream()
                 .map(userId -> createMember(userId, event.chatId(), ChatType.GROUP))
                 .toList();

        memberRepository.saveAll(members);
    }

    @Transactional
    public void createMembers(GroupChatMembersAddedEvent event) {
        List<ChatMember> members = event.userIds().stream()
                .map(userId -> createMember(userId, event.chatId(), ChatType.GROUP))
                .toList();

        memberRepository.saveAll(members);
    }

    private ChatMember createMember(Long userId, Long chatId, ChatType chatType) {
        return ChatMember.builder()
                .userId(userId)
                .chatId(chatId)
                .chatType(chatType)
                .canSendMessage(true)
                .build();
    }

    @Transactional
    public void updateMembers(GroupChatMembersUpdatedEvent event) {
        List<Long> userIds = event.members().stream()
                 .map(GroupChatMemberInfo::userId)
                 .toList();

        List<ChatMember> members = getAllByUsersAndChat(userIds, event.chatId(), ChatType.GROUP);

        Map<Long, GroupChatMemberInfo> membersInfo = event.members().stream()
                .collect(Collectors.toMap(GroupChatMemberInfo::userId, member -> member));

        for (ChatMember member : members) {
            GroupChatMemberInfo groupChatMemberInfo = membersInfo.get(member.getUserId());

            if (member.getCanSendMessage() != groupChatMemberInfo.canSendMessage()) {
                member.serCanSendMessage(groupChatMemberInfo.canSendMessage());

                cacheInvalidationService.evictChatMember(member.getUserId(), event.chatId(), ChatType.GROUP);
            }
        }

        cacheInvalidationService.evictChatMembers(event.chatId(), ChatType.GROUP);
    }

    @Transactional(readOnly = true)
    public List<ChatMember> getAllByUsersAndChat(List<Long> userIds, Long chatId, ChatType chatType) {
        List<ChatMember> members = memberRepository.findAllByUsersAndChat(userIds, chatId, chatType);

        if (members.size() != userIds.size()) {
            throw new ChatMemberNotFoundException(
                    String.format("Some chat members not found for users: %s and %s chat: %s", userIds, chatType, chatId)
            );
        }

        return members;
    }

    @Transactional
    public void removeMembers(SavedChatDeletedEvent event) {
        List<Long> userIds = memberRepository.deleteAllByChat(event.chatId(), ChatType.SAVED.name());

        userIds.forEach(userId -> {
            cacheInvalidationService.evictChatMember(userId, event.chatId(), ChatType.SAVED);
        });

        cacheInvalidationService.evictChatMembers(event.chatId(), ChatType.SAVED);
    }

    @Transactional
    public void removeMembers(PersonalChatDeletedEvent event) {
        List<Long> userIds = memberRepository.deleteAllByChat(event.chatId(), ChatType.PERSONAL.name());

        userIds.forEach(userId -> {
            cacheInvalidationService.evictChatMember(userId, event.chatId(), ChatType.PERSONAL);
        });

        cacheInvalidationService.evictChatMembers(event.chatId(), ChatType.PERSONAL);
    }

    @Transactional
    public void removeMembers(GroupChatDeletedEvent event) {
        List<Long> userIds = memberRepository.deleteAllByChat(event.chatId(), ChatType.GROUP.name());

        userIds.forEach(userId -> {
            cacheInvalidationService.evictChatMember(userId, event.chatId(), ChatType.GROUP);
        });

        cacheInvalidationService.evictChatMembers(event.chatId(), ChatType.GROUP);
    }

    @Transactional
    public void removeMembers(GroupChatMembersDeletedEvent event) {
        List<Long> userIds =
                memberRepository.deleteAllByUsersAndChat(event.userIds(), event.chatId(), ChatType.GROUP.name());

        userIds.forEach(userId -> {
            cacheInvalidationService.evictChatMember(userId, event.chatId(), ChatType.GROUP);
        });

        cacheInvalidationService.evictChatMembers(event.chatId(), ChatType.GROUP);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CHAT_MEMBER, key = "#p0 + ':' + #p1 + ':' + #p2")
    public ChatMember getMemberByUserAndChat(Long userId, Long chatId, ChatType chatType) {
        return memberRepository.findByUserIdAndChatIdAndChatType(userId, chatId, chatType)
                .orElseThrow(() -> new ChatMemberNotFoundException(
                        String.format("ChatMember with userId: %d, chatId: %d, chatType: %s not found",
                                userId, chatId, chatType)
                ));
    }

    @Transactional
    public void updateLastReadMessage(Long userId, Long chatId, ChatType chatType, Long messageId) {
        memberRepository.updateLastReadMessageId(userId, chatId, chatType, messageId);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CHAT_MEMBERS, key = "#p0 + ':' + #p1")
    public Set<Long> getMembersIdsByChat(Long chatId, ChatType chatType) {
        return new HashSet<>(memberRepository.findUserIdsByChat(chatId, chatType));
    }
}
