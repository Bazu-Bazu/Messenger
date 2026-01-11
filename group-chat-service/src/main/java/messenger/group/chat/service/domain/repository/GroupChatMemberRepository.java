package messenger.group.chat.service.domain.repository;

import messenger.group.chat.service.domain.entity.GroupChatMember;
import messenger.group.chat.service.domain.enums.GroupMemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface GroupChatMemberRepository extends JpaRepository<GroupChatMember, Long> {

    @Query("SELECT DISTINCT gm.userId " +
           "FROM GroupChatMember gm " +
           "WHERE gm.group.id = :groupId"
    )
    Set<Long> findAllUserIdsByGroupId(@Param("groupId") Long groupId);

    Optional<GroupChatMember> findByGroupIdAndUserId(Long groupId, Long userId);

    @Query("SELECT gm.role " +
           "FROM GroupChatMember gm " +
           "WHERE gm.userId IN :userIds " +
           "AND gm.group.id = :groupId")
    Set<GroupMemberRole> findRolesByUserIdsAndGroupId(
            @Param("userIds") List<Long> userIds,
            @Param("groupId") Long groupId
    );

    @Modifying
    @Query("DELETE FROM GroupChatMember gm " +
           "WHERE gm.group.id = :groupId " +
           "AND gm.userId IN :userIds")
    void deleteByUserIdsAndGroupId(@Param("userIds") List<Long> userIds, @Param("groupId") Long groupId);

    List<GroupChatMember> findAllByGroupId(Long groupId);

    boolean existsByGroupIdAndUserId(Long groupId, Long userId);

    @Modifying
    @Query("UPDATE GroupChatMember gm " +
           "SET gm.role = :role " +
           "WHERE gm.group.id = :groupId " +
           "AND gm.userId IN :userIds")
    void setRoleByUserIdsAndGroupId(
            @Param("role") GroupMemberRole role,
            @Param("userIds") List<Long> userIds,
            @Param("groupId") Long groupId
    );

    @Query("SELECT gm FROM GroupChatMember gm " +
            "WHERE gm.userId IN :userIds " +
            "AND gm.group.id = :groupId")
    List<GroupChatMember> findAllByUserIdsAndGroupId(
            @Param("userIds") List<Long> userIds,
            @Param("groupId") Long groupId
    );

}
