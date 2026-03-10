package messenger.message.service.service;

import lombok.RequiredArgsConstructor;
import enums.ChatType;
import messenger.message.service.domain.repository.MessageRepository;
import dto.response.MessageResponse;
import messenger.message.service.mapper.MessageMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageQueryService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    private static final int PAGE_SIZE = 50;
    private static final String CACHE_NAME = "chatMessages";

    @Cacheable(value = CACHE_NAME, key = "#p0 + ':' + #p1 + ':' + #p2")
    public List<MessageResponse> getCachedMessages(Long chatId, ChatType chatType, int page) {
        Pageable pageable = PageRequest.of(
                page,
                PAGE_SIZE,
                Sort.by("createdAt").descending()
        );

        return messageRepository.findByChatIdAndChatType(chatId, chatType, pageable).stream()
                .map(messageMapper::toMessageResponse)
                .toList();
    }
}
