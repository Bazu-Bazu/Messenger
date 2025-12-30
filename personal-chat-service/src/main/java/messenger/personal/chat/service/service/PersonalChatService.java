package messenger.personal.chat.service.service;

import exception.AuthorizationException;
import lombok.RequiredArgsConstructor;
import messenger.personal.chat.service.client.grpc.UserGrpcClient;
import messenger.personal.chat.service.domain.entity.PersonalChat;
import messenger.personal.chat.service.domain.repository.PersonalChatRepository;
import messenger.personal.chat.service.dto.response.PersonalChatResponse;
import messenger.personal.chat.service.exception.PersonalChatNotFoundException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonalChatService {

    private final PersonalChatRepository personalChatRepository;
    private final UserGrpcClient userGrpcClient;
    private final CacheManager cacheManager;

    @Transactional
    public PersonalChatResponse getOrCreatePersonalChat(Long user1Id, Long user2Id) {
        Optional<PersonalChat> existingChat = personalChatRepository.findPersonalChatByUsers(user1Id, user2Id);

        if (existingChat.isPresent()) {
            return createPersonalChatResponse(existingChat.get());
        }

        return createPersonalChat(user1Id, user2Id);
    }

    @Transactional
    public PersonalChatResponse createPersonalChat(Long user1Id, Long user2Id) {
        userGrpcClient.validateUsersExist(List.of(user1Id, user2Id));

        PersonalChat newChat = PersonalChat.builder()
                .user1Id(user1Id)
                .user2Id(user2Id)
                .build();

        PersonalChat savedChat = personalChatRepository.save(newChat);

        evictUserChatsCache(user1Id);
        evictUserChatsCache(user2Id);

        return createPersonalChatResponse(savedChat);
    }

    private void evictUserChatsCache(Long userId) {
        Cache cache = cacheManager.getCache("userPersonalChats");
        if (cache != null) {
            cache.evict(userId);
        }
    }

    @Cacheable(value = "userPersonalChats", key = "#userId")
    public List<PersonalChatResponse> getAllUserPersonalChats(Long userId) {
        userGrpcClient.validateUsersExist(List.of(userId));

        List<PersonalChat> personalChats = personalChatRepository.findAllUserChats(userId);

        return personalChats.stream()
                .map(this::createPersonalChatResponse)
                .toList();
    }

    @Transactional
    @CacheEvict(value = "userPersonalChats", key = "#userId")
    public void deletePersonalChat(Long userId, Long chatId) {
        userGrpcClient.validateUsersExist(List.of(userId));

        PersonalChat chat = personalChatRepository.findById(chatId)
                .orElseThrow(() -> new PersonalChatNotFoundException(
                        String.format("Personal chat with id %d not found", chatId)
                ));

        validateUserHasRightsToTheChat(chat, userId);

        personalChatRepository.delete(chat);
    }

    private void validateUserHasRightsToTheChat(PersonalChat chat, Long userId) {
        if (!chat.getUser1Id().equals(userId) && !chat.getUser2Id().equals(userId)) {
            throw new AuthorizationException(
                    String.format("User %d has no rights to the chat %d", userId, chat.getId())
            );
        }
    }

    private PersonalChatResponse createPersonalChatResponse(PersonalChat chat) {
        return PersonalChatResponse.builder()
                .id(chat.getId())
                .user1Id(chat.getUser1Id())
                .user2Id(chat.getUser2Id())
                .lastActivityAt(chat.getLastActivityAt())
                .createdAt(chat.getCreatedAt())
                .build();
    }

}
