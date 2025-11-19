package messenger.chat.service.repository;

import messenger.chat.service.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findPrivateChatBetweenUsers(Long user1Id, Long user2Id);

}
