package com.burkina.messenger.domain.repository;

import com.burkina.common.enums.messenger.ChatType;
import com.burkina.messenger.domain.entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {

    @Query("""
          SELECT cm FROM ChatMember cm
          WHERE cm.userId IN :userIds
          AND cm.chatId = :chatId
          AND cm.chatType = :chatType
    """)
    List<ChatMember> findAllByUsersAndChat(
            @Param("userIds") List<Long> userIds,
            @Param("chatId") Long chatId,
            @Param("chatType") ChatType chatType
    );

    @Query(value = """
            DELETE FROM chat_members
            WHERE user_id IN (:userIds)
              AND chat_id = :chatId
              AND chat_type = :chatType
            RETURNING user_id
    """, nativeQuery = true)
    List<Long> deleteAllByUsersAndChat(
            @Param("userIds") List<Long> userIds,
            @Param("chatId") Long chatId,
            @Param("chatType") String chatType
    );

    @Query(value = """
            DELETE FROM chat_members
            WHERE chat_id = :chatId
              AND chat_type = :chatType
            RETURNING user_id
    """, nativeQuery = true)
    List<Long> deleteAllByChat(
            @Param("chatId") Long chatId,
            @Param("chatType") String chatType
    );

    Optional<ChatMember> findByUserIdAndChatIdAndChatType(Long userId, Long chatId, ChatType chatType);

    @Modifying
    @Query(value = """
        UPDATE ChatMember cm
        SET cm.lastReadMessageId = :lastReadMessageId
        WHERE cm.userId = :userId
          AND cm.chatId = :chatId
          AND cm.chatType = :chatType
          AND (
              cm.lastReadMessageId IS NULL
              OR cm.lastReadMessageId < :messageId
          )
    """)
    void updateLastReadMessageId(
            @Param("userId") Long userId,
            @Param("chatId") Long chatId,
            @Param("chatType") ChatType chatType,
            @Param("lastReadMessageId") Long lastReadMessageId
    );
}
