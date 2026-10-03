package com.burkina.messenger.service;

import lombok.RequiredArgsConstructor;
import com.burkina.messenger.domain.entity.SavedChat;
import com.burkina.messenger.domain.repository.SavedChatRepository;
import com.burkina.messenger.exception.SavedChatNotFoundException;
import com.burkina.messenger.service.event.SavedChatPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SavedChatService {

    private final SavedChatPublisher savedChatPublisher;
    private final SavedChatRepository savedChatRepository;

    @Transactional
    public SavedChat getOrCreate(Long userId) {
        Optional<SavedChat> existingChat = savedChatRepository.findByUserId(userId);

        if (existingChat.isPresent()) {
            SavedChat chat = existingChat.get();
            if (chat.isDeleted()) {
                chat.cancelDeletion();

                savedChatPublisher.publishSavedChatCreated(chat);
            }

            return chat;
        }

        return create(userId);
    }

    private SavedChat create(Long userId) {
        SavedChat newChat = SavedChat.builder()
                .userId(userId)
                .build();

        SavedChat savedChat = savedChatRepository.save(newChat);

        savedChatPublisher.publishSavedChatCreated(savedChat);

        return savedChat;
    }

    @Transactional
    public void delete(Long userId) {
        SavedChat chat = findSavedChatByUserId(userId);

        boolean deleted = chat.delete();

        if (deleted) {
            savedChatPublisher.publishSavedChatDeleted(chat);
        }
    }

    @Transactional(readOnly = true)
    public SavedChat findSavedChatByUserId(Long userId) {
        return savedChatRepository.findByUserId(userId)
                .orElseThrow(() -> new SavedChatNotFoundException(
                        String.format("Saved chat by user %d not found", userId)
                ));
    }
}
