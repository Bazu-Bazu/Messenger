package messenger.group.chat.service.domain.repository;

import messenger.group.chat.service.domain.entity.GroupChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GroupChatMemberRepository extends JpaRepository<GroupChatMember, Long> {

    Set<Long> findAllUserIdsByGroupId(Long groupId);

}
