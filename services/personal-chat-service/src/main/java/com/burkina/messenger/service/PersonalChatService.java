package com.burkina.messenger.service;

import com.burkina.messenger.validator.UserRightsValidator;
import com.burkina.messenger.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import com.burkina.messenger.domain.entity.PersonalChat;
import com.burkina.messenger.domain.repository.PersonalChatRepository;
import com.burkina.messenger.dto.request.CreatePersonalChatRequest;
import com.burkina.messenger.exception.IllegalRequestExcepion;
import com.burkina.messenger.exception.PersonalChatNotFoundException;
import com.burkina.messenger.service.event.PersonalChatPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonalChatService {

    private final UserValidator userValidator;
    private final UserRightsValidator rightsValidator;
    private final PersonalChatPublisher personalChatPublisher;
    private final PersonalChatRepository personalChatRepository;

    @Transactional
    public PersonalChat getOrCreate(Long user1Id, CreatePersonalChatRequest request) {
        Long user2Id = request.userId();

        if (Objects.equals(user1Id, user2Id)) {
            throw new IllegalRequestExcepion("It is impossible to create a personal chat with yourself");
        }

        Optional<PersonalChat> existingChat = personalChatRepository.findPersonalChatByUsers(user1Id, user2Id);

        if (existingChat.isPresent()) {
            PersonalChat chat = existingChat.get();
            if (chat.isDeleted()) {
                chat.cancelDeletion();

                personalChatPublisher.publishPersonalChatCreated(chat);
            }

            return chat;
        }

        return create(user1Id, user2Id);
    }

    private PersonalChat create(Long user1Id, Long user2Id) {
        userValidator.validateUser(user2Id);

        PersonalChat newChat = PersonalChat.builder()
                .user1Id(user1Id)
                .user2Id(user2Id)
                .build();

        PersonalChat savedChat = personalChatRepository.save(newChat);

        personalChatPublisher.publishPersonalChatCreated(newChat);

        return savedChat;
    }

    @Transactional
    public void delete(Long userId, Long chatId) {
        PersonalChat chat = findPersonalChatById(chatId);

        rightsValidator.validateUserHasRightsToTheChat(chat, userId);

        boolean deleted = chat.delete();

        if (deleted) {
            personalChatPublisher.publishPersonalChatDeleted(chat);
        }
    }

    @Transactional(readOnly = true)
    public PersonalChat findPersonalChatById(Long chatId) {
        return personalChatRepository.findById(chatId)
                .orElseThrow(() -> new PersonalChatNotFoundException(
                        String.format("Personal chat %d not found", chatId)
                ));
    }
}
