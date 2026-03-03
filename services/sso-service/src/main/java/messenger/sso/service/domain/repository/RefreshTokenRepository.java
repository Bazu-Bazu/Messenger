package messenger.sso.service.domain.repository;

import messenger.sso.service.domain.entity.RefreshToken;
import messenger.sso.service.domain.entity.SsoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("""
        DELETE RefreshToken rt
        WHERE rt.token = :token
    """)
    void deleteToken(@Param("token") String token);

    @Modifying
    @Query("""
        DELETE RefreshToken rt
        WHERE rt.user.id = :userId
    """)
    void deleteAllByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT r FROM RefreshToken r
        WHERE r.user = :user
        ORDER BY r.createdAt ASC
    """)
    List<RefreshToken> findAllByUserOrderByCreatedAtAsc(SsoUser user);

    @Modifying
    @Query("""
        UPDATE RefreshToken rt
        SET rt.used = TRUE
        WHERE rt.token = :token AND rt.used = FALSE
    """)
    int markAsUsed(@Param("token") String token);
}
