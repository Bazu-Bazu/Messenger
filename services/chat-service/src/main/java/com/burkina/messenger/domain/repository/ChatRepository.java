package com.burkina.messenger.domain.repository;

import com.burkina.common.enums.messenger.ChatType;
import com.burkina.messenger.domain.entity.Chat;
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
            c.name = :name,
            c.avatarId = :avatarId
        WHERE c.chatId = :chatId
        AND c.chatType = :chatType
        AND (
                (c.name IS DISTINCT FROM :name)
                OR
                (c.avatarId IS DISTINCT FROM :avatarId)
            )
    """)
    int changeInfoForGroup(
            @Param("chatId") Long chatId,
            @Param("chatType") ChatType chatType,
            @Param("name") String name,
            @Param("avatarId") Long avatarId
    );
}
