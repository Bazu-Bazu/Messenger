package com.burkina.messenger.service;

import com.burkina.common.enums.messenger.ChatType;
import com.burkina.messenger.domain.repository.UserChatRepository;
import com.burkina.messenger.dto.response.ChatResponse;
import lombok.RequiredArgsConstructor;
import com.burkina.messenger.domain.entity.Chat;
import com.burkina.messenger.domain.repository.ChatRepository;
import com.burkina.messenger.exception.ChatNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserChatRepository userChatRepository;

    @Transactional(readOnly = true)
    public Chat findChatByIdAndType(Long chatId, ChatType chatType) {
        return chatRepository.findChatByChatIdAndChatType(chatId, chatType)
                .orElseThrow(() -> new ChatNotFoundException(
                            String.format("%s chat %d not found", chatType, chatId)
                ));
    }

    @Transactional(readOnly = true)
    public List<ChatResponse> getUserChats(Long userId) {
        List<ChatResponse> personalChats = userChatRepository.findPersonalChatsByUserId(userId);
        List<ChatResponse> groupAndSavedChats = userChatRepository.findGroupAndSavedChatsByUserId(userId);

        return Stream.concat(
                personalChats.stream(),
                groupAndSavedChats.stream()
        ).toList();
    }
}
