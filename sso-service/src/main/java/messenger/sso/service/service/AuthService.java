package messenger.sso.service.service;

import exception.AuthorizationException;
import lombok.RequiredArgsConstructor;
import messenger.sso.service.domain.entity.SsoUser;
import messenger.sso.service.dto.request.RefreshTokenRequest;
import messenger.sso.service.dto.request.SignInRequest;
import messenger.sso.service.dto.response.AuthResponse;
import messenger.sso.service.exception.RefreshTokenException;
import messenger.sso.service.jwt.CustomUserDetails;
import messenger.sso.service.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final SsoUserService ssoUserService;
    private final JwtService jwtService;

    public AuthResponse signIn(SignInRequest request, String deviceInfo, String ipAddress) {
        SsoUser ssoUser = ssoUserService.findSsoUserByPhone(request.phone());

        if (!ssoUser.isEnabled()) {
            throw new AuthorizationException(
                    String.format("User with phone %s not verified", request.phone())
            );
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.phone(),
                        request.password()
                )
        );

        return generateTokens(ssoUser, deviceInfo, ipAddress);
    }

    public AuthResponse refresh(RefreshTokenRequest request, String deviceInfo, String ipAddress) {
        String refreshToken = request.refreshToken();
        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new RefreshTokenException(
                    String.format("Provided token %s is not a refresh token", refreshToken)
            );
        }

        refreshTokenService.verifyActivity(refreshToken);
        refreshTokenService.revokeToken(refreshToken);

        String phone = jwtService.extractUsername(refreshToken);
        SsoUser ssoUser = ssoUserService.findSsoUserByPhone(phone);

        return generateTokens(ssoUser, deviceInfo, ipAddress);
    }

    private AuthResponse generateTokens(SsoUser ssoUser, String deviceInfo, String ipAddress) {
        UserDetails userDetails = new CustomUserDetails(ssoUser);
        String newAccessToken = jwtService.generateAccessToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        refreshTokenService.addRefreshToken(ssoUser, newRefreshToken, deviceInfo, ipAddress);

        return createAuthResponse(ssoUser.getId(), newAccessToken, newRefreshToken);
    }

    public void logout(RefreshTokenRequest request) {
        refreshTokenService.revokeToken(request.refreshToken());
    }

    private AuthResponse createAuthResponse(Long userId, String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .userId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
