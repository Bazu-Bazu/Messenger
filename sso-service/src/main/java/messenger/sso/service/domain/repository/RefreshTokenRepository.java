package messenger.sso.service.domain.repository;

import messenger.sso.service.domain.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("UPDATE RefreshToken rt " +
           "SET rt.revokedAt = :revokedAt " +
           "WHERE rt.user.id = :userId " +
           "AND rt.revokedAt IS NULL"
    )
    void revokeAllUserTokens(@Param("userId") Long userId, @Param("revokedAt") Instant revokedAt);

    @Modifying
    @Query("UPDATE RefreshToken rt " +
           "SET rt.revokedAt = :revokedAt " +
           "WHERE rt.user.id = :userId " +
           "AND rt.deviceInfo = :deviceInfo " +
           "AND rt.revokedAt IS NULL"
    )
    void revokeTokenByDevice(
            @Param("userId") Long userId,
            @Param("deviceInfo") String deviceInfo,
            @Param("revokedAt") Instant revokedAt
    );

    @Modifying
    @Query("UPDATE RefreshToken rt " +
           "SET rt.revokedAt = :revokedAt " +
           "WHERE rt.token = :token " +
           "AND rt.revokedAt IS NULL"
    )
    void revokeToken(@Param("token") String token, @Param("revokedAt") Instant revokedAt);

}
