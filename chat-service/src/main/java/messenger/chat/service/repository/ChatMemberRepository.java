package messenger.chat.service.repository;

import messenger.chat.service.entity.Chat;
import messenger.chat.service.entity.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {

    List<Chat> findChatsByUserId(Long userId);
    List<ChatMember> findByChatId(Long chatId);
    boolean existByChatIdAndUserId(Long chatId, Long userId);
    Optional<ChatMember> findByChatIdAndUserId(Long chatId, Long userId);

}
