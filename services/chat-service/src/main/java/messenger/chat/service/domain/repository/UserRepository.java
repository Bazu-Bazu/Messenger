package messenger.chat.service.domain.repository;

import messenger.chat.service.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("""
        UPDATE User u
        SET u.avatarId = :avatarId
        WHERE u.userId = :userId
    """)
    void updateAvatarId(@Param("userId") Long userId, @Param("avatarId") Long avatarId);
}
