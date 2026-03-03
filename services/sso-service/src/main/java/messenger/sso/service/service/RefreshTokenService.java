package messenger.sso.service.service;

import lombok.RequiredArgsConstructor;
import messenger.sso.service.domain.entity.RefreshToken;
import messenger.sso.service.domain.entity.SsoUser;
import messenger.sso.service.domain.repository.RefreshTokenRepository;
import messenger.sso.service.exception.RefreshTokenNotFoundException;
import messenger.sso.service.exception.RefreshTokenReuseException;
import messenger.sso.service.security.jwt.JwtService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final ApplicationContext context;
    private final JwtService jwtService;

    private final int MAX_ACTIVE_DEVICES = 5;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public RefreshToken useRefreshToken(String token) {
        int updated = refreshTokenRepository.markAsUsed(token);

        if (updated == 0) {
            RefreshToken stolenToken = findRefreshToken(token);

            RefreshTokenService proxy = context.getBean(RefreshTokenService.class);
            proxy.deleteAllByUserIdInNewTx(stolenToken.getUser().getId());

            throw new RefreshTokenReuseException(
                    String.format("Refresh token %s reuse detected", token)
            );
        }

        return findRefreshToken(token);
    }

    @Transactional
    public void addRefreshToken(SsoUser ssoUser, String token, String deviceInfo, String ipAddress) {
        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserOrderByCreatedAtAsc(ssoUser);

        if (tokens.size() >= MAX_ACTIVE_DEVICES) {
            RefreshToken oldest = tokens.get(0);
            refreshTokenRepository.delete(oldest);
        }

        Instant expiresAt = Instant.now().plusMillis(jwtService.getRefreshTokenExpiration());

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(ssoUser)
                .expiresAt(expiresAt)
                .deviceInfo(deviceInfo)
                .ipAddress(ipAddress)
                .build();

        refreshTokenRepository.save(refreshToken);
    }

    @Transactional(readOnly = true)
    public RefreshToken findRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException(
                        String.format("Refresh token %s not found", token)
                ));
    }

    @Transactional
    public void deleteToken(String token) {
        refreshTokenRepository.deleteToken(token);
    }

    @Transactional
    public void deleteAllByUserId(Long userId) {
        refreshTokenRepository.deleteAllByUserId(userId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteAllByUserIdInNewTx(Long userId) {
        refreshTokenRepository.deleteAllByUserId(userId);
    }
}
