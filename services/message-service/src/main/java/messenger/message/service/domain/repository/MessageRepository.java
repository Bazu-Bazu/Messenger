package messenger.message.service.domain.repository;

import messenger.message.service.domain.entity.Message;
import enums.ChatType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChatIdAndChatType(Long chatId, ChatType chatType, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE Message m
        SET m.readAt = :readAt
        WHERE m.id = :id
        AND m.readAt IS NULL
    """)
    void markAsRead(Long id, Instant readAt);
}
