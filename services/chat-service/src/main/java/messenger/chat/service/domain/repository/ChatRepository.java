package messenger.chat.service.domain.repository;

import enums.ChatType;
import messenger.chat.service.domain.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    int deleteByChatIdAndChatType(Long chatId, ChatType chatType);
    Optional<Chat> findChatByChatIdAndChatType(Long chatId, ChatType chatType);

    @Modifying
    @Query("""
        UPDATE Chat c
        SET
            c.title = :title,
            c.avatarId = :avatarId
        WHERE c.chatId = :chatId
        AND c.chatType = :chatType
        AND (
                (c.title IS DISTINCT FROM :title)
                OR
                (c.avatarId IS DISTINCT FROM :avatarId)
            )
    """)
    int changeInfoForGroup(
            @Param("chatId") Long chatId,
            @Param("chatType") ChatType chatType,
            @Param("title") String title,
            @Param("avatarId") Long avatarId
    );
}
