package messenger.group.chat.service.domain.repository;

import messenger.group.chat.service.domain.entity.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {

    @Query("SELECT gc FROM GroupChat gc " +
           "JOIN gc.members gm " +
           "WHERE gm.userId = :userId")
    List<GroupChat> findAllUserChatIds(@Param("userId") Long userId);

}
