package messenger.personal.chat.service.domain.repository;

import messenger.personal.chat.service.domain.entity.PersonalChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalChatRepository extends JpaRepository<PersonalChat, Long> {

    @Query("""
        SELECT pc FROM PersonalChat pc
        WHERE (pc.user1Id = :user1Id AND pc.user2Id = :user2Id)
        OR (pc.user1Id = :user2Id AND pc.user2Id = :user1Id)
    """)
    Optional<PersonalChat> findPersonalChatByUsers(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    @Query("""
        SELECT pc FROM PersonalChat pc
        WHERE (pc.user1Id = :userId OR pc.user2Id = :userId)
    """)
    List<PersonalChat> findAllUserChats(@Param("userId") Long userId);

    @Query("""
        SELECT COUNT(pc) > 0 FROM PersonalChat pc
        WHERE (pc.user1Id = :userId OR pc.user2Id = :userId)
        AND pc.id = :chatId
    """)
    boolean existsMemberByChatIdAndUserId(@Param("chatId") Long chatId, @Param("userId") Long userId);

    @Query("""
        SELECT pc.user1Id FROM PersonalChat pc WHERE pc.id = :chatId
        UNION ALL
        SELECT pc.user2Id FROM PersonalChat pc WHERE pc.id = :chatId
    """)
    List<Long> findUserIdsByChatId(@Param("chatId") Long chatId);
}
