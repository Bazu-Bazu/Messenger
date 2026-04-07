package messenger.chat.service.domain.repository;

import messenger.chat.service.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("""
        UPDATE User u
        SET u.avatarId = :avatarId
        WHERE u.userId = :userId
    """)
    int updateAvatarId(@Param("userId") Long userId, @Param("avatarId") Long avatarId);

    @Query("""
        SELECT u FROM User u
        WHERE u.userId IN :userIds
    """)
    List<User> findUsersByIds(@Param("userIds") List<Long> userIds);
}
