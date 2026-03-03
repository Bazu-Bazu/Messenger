package messenger.sso.service.service;

import lombok.RequiredArgsConstructor;
import messenger.sso.service.domain.entity.RefreshToken;
import messenger.sso.service.domain.entity.SsoUser;
import messenger.sso.service.dto.request.RefreshTokenRequest;
import messenger.sso.service.dto.request.LoginRequest;
import messenger.sso.service.dto.response.AuthResponse;
import messenger.sso.service.exception.AuthorizationException;
import messenger.sso.service.exception.IllegalRefreshTokenException;
import messenger.sso.service.exception.RefreshTokenExpiredException;
import messenger.sso.service.security.userDetails.CustomUserDetails;
import messenger.sso.service.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final SsoUserService ssoUserService;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse login(LoginRequest request, String deviceInfo, String ipAddress) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.phone(),
                        request.password()
                )
        );

        return generateTokens(request.phone(), deviceInfo, ipAddress);
    }

    @Transactional
    public AuthResponse refresh(RefreshTokenRequest request, String deviceInfo, String ipAddress) {
        String token = request.refreshToken();
        if (!jwtService.isRefreshToken(token)) {
            throw new IllegalRefreshTokenException(
                    String.format("Provided token %s is not a refresh token", token)
            );
        }

        RefreshToken refreshToken = refreshTokenService.useRefreshToken(token);
        if (!refreshToken.isActive()) {
            throw new RefreshTokenExpiredException(
                    String.format("Refresh token %s was expired", token)
            );
        }

        String phone = jwtService.extractUsername(token);

        return generateTokens(phone, deviceInfo, ipAddress);
    }

    private AuthResponse generateTokens(String phone, String deviceInfo, String ipAddress) {
        SsoUser ssoUser = ssoUserService.findSsoUserByPhone(phone);

        UserDetails userDetails = new CustomUserDetails(ssoUser);
        String newAccessToken = jwtService.generateAccessToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        refreshTokenService.addRefreshToken(ssoUser, newRefreshToken, deviceInfo, ipAddress);

        return createAuthResponse(ssoUser.getId(), newAccessToken, newRefreshToken);
    }

    @Transactional
    public void logout(Long userId, RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();

        RefreshToken token = refreshTokenService.findRefreshToken(refreshToken);

        if (!token.getUser().getId().equals(userId)) {
            refreshTokenService.deleteAllByUserIdInNewTx(token.getUser().getId());
            throw new AuthorizationException(
                    String.format("Token %s does not belong to user %d", refreshToken, userId)
            );
        }

        refreshTokenService.deleteToken(refreshToken);
    }

    private AuthResponse createAuthResponse(Long userId, String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .userId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
