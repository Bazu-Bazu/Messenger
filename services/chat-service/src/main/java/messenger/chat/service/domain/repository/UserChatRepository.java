package messenger.chat.service.domain.repository;

import enums.ChatMemberRole;
import enums.ChatType;
import messenger.chat.service.domain.entity.UserChat;
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
        UPDATE UserChat uc
        SET uc.role = :role
        WHERE uc.user.userId IN :userIds
        AND uc.chat.chatId = :chatId
        AND uc.chat.chatType = :chatType
    """)
    int changeRoles(
            @Param("userIds") List<Long> userIds,
            @Param("role") ChatMemberRole role,
            @Param("chatId") Long chatId,
            @Param("chatType") ChatType chatType
    );

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
}
