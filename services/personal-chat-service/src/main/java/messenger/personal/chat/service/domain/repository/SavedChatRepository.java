package messenger.personal.chat.service.domain.repository;

import messenger.personal.chat.service.domain.entity.SavedChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavedChatRepository extends JpaRepository<SavedChat, Long> {

    Optional<SavedChat> findByUserId(Long userId);
}
