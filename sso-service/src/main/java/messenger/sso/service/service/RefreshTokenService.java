package messenger.sso.service.service;

import lombok.RequiredArgsConstructor;
import messenger.sso.service.domain.entity.RefreshToken;
import messenger.sso.service.domain.entity.SsoUser;
import messenger.sso.service.domain.repository.RefreshTokenRepository;
import messenger.sso.service.domain.repository.SsoUserRepository;
import messenger.sso.service.exception.RefreshTokenException;
import messenger.sso.service.exception.SsoUserException;
import messenger.sso.service.jwt.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final SsoUserRepository ssoUserRepository;
    private final JwtService jwtService;

    @Transactional
    public void addRefreshToken(Long userId, String token, String deviceInfo, String ipAddress) {
        SsoUser ssoUser = ssoUserRepository.findById(userId)
                .orElseThrow(() -> new SsoUserException(
                        String.format("User %d not found", userId)
                ));

        Instant expiresAt = Instant.now().plusMillis(jwtService.getRefreshTokenExpiration());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUser(ssoUser);
        refreshToken.setExpiresAt(expiresAt);
        refreshToken.setCreatedAt(Instant.now());
        refreshToken.setDeviceInfo(deviceInfo);
        refreshToken.setIpAddress(ipAddress);

        refreshTokenRepository.save(refreshToken);
    }

    public void verifyActivity(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException(
                        String.format("Token %s not found", token)
                ));

        if (!refreshToken.isActive()) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException("Refresh token was expired");
        }
    }

    @Transactional
    public void revokeToken(String token) {
        refreshTokenRepository.revokeToken(token, Instant.now());
    }

    @Transactional
    public void revokeAllUserTokens(Long userId) {
        refreshTokenRepository.revokeAllUserTokens(userId, Instant.now());
    }

    @Transactional
    public void revokeTokenByDevice(Long userId, String deviceInfo) {
        refreshTokenRepository.revokeTokenByDevice(userId, deviceInfo, Instant.now());
    }

}
