package messenger.chat.service.service;

import com.messenger.grpc.User;
import lombok.RequiredArgsConstructor;
import messenger.chat.service.dto.request.CreateGroupChatResponse;
import messenger.chat.service.dto.response.ChatResponse;
import messenger.chat.service.entity.Chat;
import messenger.chat.service.entity.ChatMember;
import messenger.chat.service.entity.ChatMemberRole;
import messenger.chat.service.entity.ChatType;
import messenger.chat.service.exception.ChatException;
import messenger.chat.service.exception.UserException;
import messenger.chat.service.repository.ChatMemberRepository;
import messenger.chat.service.repository.ChatRepository;
import messenger.chat.service.service.grpc.UserServiceClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final UserServiceClient userServiceClient;

    @Cacheable(value = "chatMetadata", key = "#chatId")
    public ChatResponse getChat(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatException(
                        String.format("Chat with id %d not found", chatId)
                ));

        return createChatResponse(chat);
    }

    public ChatResponse createPrivateChat(Long user1Id, Long user2Id) {
        Optional<Chat> existingChat = chatRepository.findPrivateChatBetweenUsers(user1Id, user2Id);
        if (existingChat.isPresent()) {
            return createChatResponse(existingChat.get());
        }

        Chat newChat = Chat.builder()
                .type(ChatType.PRIVATE)
                .build();

        Chat savedChat = chatRepository.save(newChat);

        addChatMember(savedChat, user1Id, ChatMemberRole.MEMBER);
        addChatMember(savedChat, user2Id, ChatMemberRole.MEMBER);

        return createChatResponse(savedChat);
    }

    public ChatResponse createGroupChat(Long creatorUserId, CreateGroupChatResponse response) {
        Chat newChat = Chat.builder()
                .type(ChatType.GROUP)
                .name(response.name())
                .build();

        Chat firstSavedChat = chatRepository.save(newChat);

        ChatMember creator = addChatMember(firstSavedChat, creatorUserId, ChatMemberRole.OWNER);
        response.userIds().forEach(userId -> addChatMember(firstSavedChat, userId, ChatMemberRole.MEMBER));

        firstSavedChat.setCreatedBy(creator.getId());
        Chat secondSavedChat = chatRepository.save(firstSavedChat);

        return createChatResponse(secondSavedChat);
    }

    public List<ChatResponse> getUserChats(Long userId) {
        List<Chat> chats = chatMemberRepository.findChatsByUserId(userId);

        return chats.stream()
                .map(this::createChatResponse)
                .toList();
    }

    private ChatMember addChatMember(Chat chat, Long userId, ChatMemberRole role) {
        User.UserExistResponse response = userServiceClient.userExist(userId);

        if (!response.getUserExist()) {
            throw new UserException(
                    String.format("User with id %d not found", userId)
            );
        }

        ChatMember newMember = ChatMember.builder()
                .chat(chat)
                .userId(userId)
                .role(role)
                .userUsername(response.getUsername())
                .build();

        return chatMemberRepository.save(newMember);
    }

    private ChatResponse createChatResponse(Chat chat) {
        return ChatResponse.builder()
                .id(chat.getId())
                .name(chat.getName())
                .avatarUrl(chat.getAvatarUrl())
                .description(chat.getDescription())
                .createdAt(chat.getCreatedAt())
                .updatedAt(chat.getUpdatedAt())
                .createdBy(chat.getCreatedBy())
                .type(chat.getType())
                .membersIds(chat.getMembers().stream()
                        .map(ChatMember::getId)
                        .toList())
                .build();
    }

}
