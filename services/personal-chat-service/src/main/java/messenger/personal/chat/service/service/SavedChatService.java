package messenger.personal.chat.service.service;

import lombok.RequiredArgsConstructor;
import messenger.personal.chat.service.domain.entity.SavedChat;
import messenger.personal.chat.service.domain.repository.SavedChatRepository;
import messenger.personal.chat.service.dto.response.SavedChatResponse;
import messenger.personal.chat.service.exception.SavedChatNotFoundException;
import messenger.personal.chat.service.service.event.SavedChatPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SavedChatService {

    private final SavedChatRepository savedChatRepository;
    private final SavedChatPublisher savedChatPublisher;

    @Transactional
    public SavedChatResponse getOrCreate(Long userId) {
        Optional<SavedChat> existingChat = savedChatRepository.findByUserId(userId);

        if (existingChat.isPresent()) {
            SavedChat chat = existingChat.get();
            if (chat.isDeleted()) {
                chat.setDeleted(false);

                savedChatPublisher.publishChatCreated(chat);
            }

            return createSavedChatResponse(chat);
        }

        return create(userId);
    }

    private SavedChatResponse create(Long userId) {
        SavedChat newChat = SavedChat.builder()
                .userId(userId)
                .build();

        SavedChat savedChat = savedChatRepository.save(newChat);

        savedChatPublisher.publishChatCreated(savedChat);

        return createSavedChatResponse(savedChat);
    }

    @Transactional
    public void delete(Long userId) {
        SavedChat chat = findSavedChatByUserId(userId);

        chat.setDeleted(true);

        savedChatPublisher.publishChatDeleted(chat);
    }

    @Transactional(readOnly = true)
    public SavedChat findSavedChatByUserId(Long userId) {
        return savedChatRepository.findByUserId(userId)
                .orElseThrow(() -> new SavedChatNotFoundException(
                        String.format("Saved chat by user %d not found", userId)
                ));
    }

    private SavedChatResponse createSavedChatResponse(SavedChat chat) {
        return SavedChatResponse.builder()
                .id(chat.getId())
                .userId(chat.getUserId())
                .createdAt(chat.getCreatedAt())
                .build();
    }
}
