package messenger.personal.chat.service.service;

import lombok.RequiredArgsConstructor;
import messenger.personal.chat.service.domain.entity.PersonalChat;
import messenger.personal.chat.service.domain.repository.PersonalChatRepository;
import messenger.personal.chat.service.dto.request.CreatePersonalChatRequest;
import messenger.personal.chat.service.dto.response.PersonalChatResponse;
import messenger.personal.chat.service.exception.IllegalRequestExcepion;
import messenger.personal.chat.service.exception.PersonalChatNotFoundException;
import messenger.personal.chat.service.service.event.PersonalChatPublisher;
import messenger.personal.chat.service.service.validator.UserValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonalChatService {

    private final PersonalChatRepository personalChatRepository;
    private final PersonalChatPublisher personalChatPublisher;
    private final UserValidator userValidator;

    @Transactional
    public PersonalChatResponse getOrCreate(Long user1Id, CreatePersonalChatRequest request) {
        Long user2Id = request.userId();

        if (Objects.equals(user1Id, user2Id)) {
            throw new IllegalRequestExcepion("It is impossible to create a personal chat with yourself");
        }

        Optional<PersonalChat> existingChat = personalChatRepository.findPersonalChatByUsers(user1Id, user2Id);

        if (existingChat.isPresent()) {
            PersonalChat chat = existingChat.get();
            if (chat.isDeleted()) {
                chat.setDeleted(false);

                personalChatPublisher.publishChatCreated(chat);
            }

            return createPersonalChatResponse(chat);
        }

        return create(user1Id, user2Id);
    }

    private PersonalChatResponse create(Long user1Id, Long user2Id) {
        userValidator.validateUsersExist(List.of(user2Id));

        PersonalChat newChat = PersonalChat.builder()
                .user1Id(user1Id)
                .user2Id(user2Id)
                .build();

        PersonalChat savedChat = personalChatRepository.save(newChat);

        personalChatPublisher.publishChatCreated(newChat);

        return createPersonalChatResponse(savedChat);
    }

    @Transactional
    public void delete(Long userId, Long chatId) {
        PersonalChat chat = findPersonalChatById(chatId);

        userValidator.validateUserHasRightsToTheChat(chat, userId);

        chat.setDeleted(true);

        personalChatPublisher.publishChatDeleted(chat);
    }

    @Transactional(readOnly = true)
    public PersonalChat findPersonalChatById(Long chatId) {
        return personalChatRepository.findById(chatId)
                .orElseThrow(() -> new PersonalChatNotFoundException(
                        String.format("Personal chat %d not found", chatId)
                ));
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
                .createdAt(chat.getCreatedAt())
                .build();
    }
}
