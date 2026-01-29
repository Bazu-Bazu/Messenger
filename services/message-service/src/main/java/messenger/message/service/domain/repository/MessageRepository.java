package messenger.message.service.domain.repository;

import messenger.message.service.domain.entity.Message;
import enums.ChatType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChatIdAndChatType(Long chatId, ChatType chatType, Pageable pageable);
    Optional<Message> findByIdAndChatIdAndChatType(Long messageId, Long chatId, ChatType chatType);

}
