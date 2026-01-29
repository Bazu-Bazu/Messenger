package messenger.sso.service.service;

import lombok.RequiredArgsConstructor;
import messenger.sso.service.domain.entity.RefreshToken;
import messenger.sso.service.domain.entity.SsoUser;
import messenger.sso.service.domain.repository.RefreshTokenRepository;
import messenger.sso.service.exception.RefreshTokenException;
import messenger.sso.service.jwt.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Transactional
    public void addRefreshToken(SsoUser user, String token, String deviceInfo, String ipAddress) {
        Instant expiresAt = Instant.now().plusMillis(jwtService.getRefreshTokenExpiration());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(expiresAt);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setDeviceInfo(deviceInfo);
        refreshToken.setIpAddress(ipAddress);

        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException(
                        String.format("Refresh token %s not found", token)
                ));
    }

    public void verifyActivity(String token) {
        RefreshToken refreshToken = findRefreshToken(token);

        if (!refreshToken.isActive()) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException(
                    String.format("Refresh token %s was expired", token)
            );
        }
    }

    @Transactional
    public void revokeToken(String token) {
        refreshTokenRepository.revokeToken(token, Instant.now());
    }

}
