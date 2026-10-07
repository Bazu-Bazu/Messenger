package com.burkina.messenger.domain.repository;

import com.burkina.common.enums.messenger.ChatType;
import com.burkina.messenger.domain.entity.UserChat;
import com.burkina.messenger.dto.response.ChatResponse;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChatRepository extends JpaRepository<UserChat, Long> {

    @Modifying
    @Query("""
        DELETE UserChat uc
        WHERE uc.user.userId IN :userIds
        AND uc.chat.chatId = :chatId
        AND uc.chat.chatType = :chatType
    """)
    int deleteUsersChats(
            @Param("userIds") List<Long> userIds,
            @Param("chatId") Long chatId,
            @Param("chatType") ChatType chatType
    );

    @EntityGraph(attributePaths = {"user"})
    @Query("""
        SELECT uc FROM UserChat uc
        WHERE uc.user.userId IN :userIds
        AND uc.chat.chatId = :chatId
        AND uc.chat.chatType = "GROUP"
    """)
    List<UserChat> findByUserIdsAndGroupChat(
            @Param("userIds") List<Long> userIds,
            @Param("chatId") Long chatId
    );

    @Query("""
        SELECT new com.burkina.messenger.dto.response.ChatResponse(
            c.chatId,
            c.chatType,
            otherUser.username,
            otherUser.avatarId
        )
        FROM UserChat uc
        JOIN uc.chat c
        LEFT JOIN UserChat otherUc
            ON otherUc.chat = uc.chat
            AND otherUc.user.userId <> :userId
        LEFT JOIN otherUc.user otherUser
        WHERE uc.user.userId = :userId
          AND c.chatType = com.burkina.common.enums.messenger.ChatType.PERSONAL
    """)
    List<ChatResponse> findPersonalChatsByUserId(
            @Param("userId") Long userId
    );

    @Query("""
        SELECT new com.burkina.messenger.dto.response.ChatResponse(
            c.chatId,
            c.chatType,
            c.name,
            c.avatarId
        )
        FROM UserChat uc
        JOIN uc.chat c
        WHERE uc.user.userId = :userId
          AND c.chatType IN (
              com.burkina.common.enums.messenger.ChatType.GROUP,
              com.burkina.common.enums.messenger.ChatType.SAVED
          )
    """)
    List<ChatResponse> findGroupAndSavedChatsByUserId(
            @Param("userId") Long userId
    );
}
