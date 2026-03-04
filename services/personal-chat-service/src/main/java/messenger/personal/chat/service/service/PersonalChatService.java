package messenger.personal.chat.service.service;

import lombok.RequiredArgsConstructor;
import messenger.personal.chat.service.domain.entity.PersonalChat;
import messenger.personal.chat.service.domain.repository.PersonalChatRepository;
import messenger.personal.chat.service.dto.request.CreatePersonalChatRequest;
import messenger.personal.chat.service.dto.response.PersonalChatResponse;
import messenger.personal.chat.service.exception.PersonalChatNotFoundException;
import messenger.personal.chat.service.service.validator.UserValidator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonalChatService {

    private final PersonalChatRepository personalChatRepository;
    private final PersonalChatCacheService personalChatCacheService;
    private final UserValidator userValidator;

    private static final String CACHE_VALUE = "userPersonalChats";

    @Transactional
    public PersonalChatResponse getOrCreatePersonalChat(Long user1Id, CreatePersonalChatRequest request) {
        Long user2Id = request.userId();

        Optional<PersonalChat> existingChat = personalChatRepository.findPersonalChatByUsers(user1Id, user2Id);

        if (existingChat.isPresent()) {
            return createPersonalChatResponse(existingChat.get());
        }

        return createPersonalChat(user1Id, user2Id);
    }

    @Transactional
    public PersonalChatResponse createPersonalChat(Long user1Id, Long user2Id) {
        userValidator.validateUsersExist(List.of(user2Id));

        PersonalChat newChat = PersonalChat.builder()
                .user1Id(user1Id)
                .user2Id(user2Id)
                .build();

        PersonalChat savedChat = personalChatRepository.save(newChat);

        personalChatCacheService.evictUsersChats(user1Id, user2Id);

        return createPersonalChatResponse(savedChat);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_VALUE, key = "#p0")
    public List<PersonalChatResponse> getAllUserPersonalChats(Long userId) {
        List<PersonalChat> personalChats = personalChatRepository.findAllUserChats(userId);

        return personalChats.stream()
                .map(this::createPersonalChatResponse)
                .toList();
    }

    @Transactional
    public void deletePersonalChat(Long userId, Long chatId) {
        PersonalChat chat = findPersonalChatById(chatId);

        userValidator.validateUserHasRightsToTheChat(chat, userId);

        Long user1Id = chat.getUser1Id();
        Long user2Id = chat.getUser2Id();

        personalChatRepository.delete(chat);

        personalChatCacheService.evictUsersChats(user1Id, user2Id);
    }

    @Transactional(readOnly = true)
    public PersonalChat findPersonalChatById(Long chatId) {
        return personalChatRepository.findById(chatId)
                .orElseThrow(() -> new PersonalChatNotFoundException(
                        String.format("Personal %d not found", chatId)
                ));
    }

    @Transactional
    public void updateLastActivity(Long chatId, Instant lastActivity) {
        PersonalChat chat = findPersonalChatById(chatId);
        chat.setLastActivityAt(lastActivity);

        Long user1Id = chat.getUser1Id();
        Long user2Id = chat.getUser2Id();

        personalChatCacheService.evictUsersChats(user1Id, user2Id);
    }

    @Transactional(readOnly = true)
    public boolean isUserMember(Long chatId, Long userId) {
        return personalChatRepository.existsMemberByChatIdAndUserId(chatId, userId);
    }

    @Transactional(readOnly = true)
    public List<Long> getAllMembers(Long chatId) {
        return personalChatRepository.findUserIdsByChatId(chatId);
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
