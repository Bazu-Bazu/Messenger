package messenger.chat.service.service;

import lombok.RequiredArgsConstructor;
import messenger.chat.service.dto.response.ChatMemberResponse;
import messenger.chat.service.entity.ChatMember;
import messenger.chat.service.exception.ChatMemberException;
import messenger.chat.service.repository.ChatMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatMemberService {

    private final ChatMemberRepository chatMemberRepository;

    public List<ChatMemberResponse> getChatMembers(Long chatId) {
        List<ChatMember> chatMembers = chatMemberRepository.findByChatId(chatId);

        return chatMembers.stream()
                .map(this::createChatMemberResponse)
                .toList();
    }

    public boolean isUserMemberOfChat(Long chatId, Long userId) {
        return chatMemberRepository.existByChatIdAndUserId(chatId, userId);
    }

    public void updateLastRead(Long chatId, Long userId) {
        ChatMember chatMember = chatMemberRepository.findByChatIdAndUserId(chatId, userId)
                .orElseThrow(() -> new ChatMemberException(
                        String.format("Chat member with userId %d and chatId %d not found", userId, chatId)
                ));

        chatMember.setLastReadAt(Instant.now());
        chatMemberRepository.save(chatMember);
    }

    private ChatMemberResponse createChatMemberResponse(ChatMember chatMember) {
        return ChatMemberResponse.builder()
                .id(chatMember.getId())
                .username(chatMember.getUserUsername())
                .chatId(chatMember.getChat().getId())
                .role(chatMember.getRole())
                .joinedAt(chatMember.getJoinedAt())
                .lastReadAt(chatMember.getLastReadAt())
                .userId(chatMember.getUserId())
                .build();
    }

}
