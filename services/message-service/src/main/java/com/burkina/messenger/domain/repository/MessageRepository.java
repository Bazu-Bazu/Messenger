package com.burkina.messenger.domain.repository;

import com.burkina.common.enums.messenger.ChatType;
import com.burkina.messenger.domain.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChatIdAndChatTypeOrderByIdDesc(Long chatId, ChatType chatType, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE Message m
        SET m.read = TRUE
        WHERE m.id = :id
    """)
    int markAsRead(Long id);

    @Query("""
        SELECT m
        FROM Message m
        WHERE m.chatId = :chatId
          AND m.chatType = :chatType
          AND m.id > :lastReadMessageId
        ORDER BY m.id ASC
    """)
    List<Message> findNextMessages(
            @Param("chatId") Long chatId,
            @Param("chatType") ChatType chatType,
            @Param("lastReadMessageId") Long lastReadMessageId,
            Pageable pageable
    );

    @Query("""
        SELECT m
        FROM Message m
        WHERE m.chatId = :chatId
          AND m.chatType = :chatType
          AND m.id <= :lastReadMessageId
        ORDER BY m.id DESC
    """)
    List<Message> findPreviousMessages(
            @Param("chatId") Long chatId,
            @Param("chatType") ChatType chatType,
            @Param("lastReadMessageId") Long lastReadMessageId,
            Pageable pageable
    );
}
