package messenger.sso.service.service;

import messenger.sso.service.domain.entity.RefreshToken;
import messenger.sso.service.domain.entity.SsoUser;
import messenger.sso.service.dto.request.LoginRequest;
import messenger.sso.service.dto.request.RefreshTokenRequest;
import messenger.sso.service.dto.response.AuthResponse;
import messenger.sso.service.exception.AuthorizationException;
import messenger.sso.service.exception.IllegalRefreshTokenException;
import messenger.sso.service.exception.RefreshTokenExpiredException;
import messenger.sso.service.security.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private SsoUserService ssoUserService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private final String PHONE = "79999999999";
    private final String PASSWORD = "password";
    private final String DEVICE = "Chrome";
    private final String IP = "127.0.0.1";

    private SsoUser user;

    @BeforeEach
    void setup() {
        user = SsoUser.builder()
                .id(1L)
                .phone(PHONE)
                .username("user")
                .password("encoded")
                .build();
    }

    @Test
    void login_shouldReturnTokens_whenCredentialsValid() {
        LoginRequest request = new LoginRequest(PHONE, PASSWORD);

        when(ssoUserService.findSsoUserByPhone(PHONE)).thenReturn(user);
        when(jwtService.generateAccessToken(any())).thenReturn("access");
        when(jwtService.generateRefreshToken(any())).thenReturn("refresh");

        AuthResponse response = authService.login(request, DEVICE, IP);

        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        verify(refreshTokenService)
                .addRefreshToken(eq(user), eq("refresh"), eq(DEVICE), eq(IP));

        assertEquals(1L, response.userId());
        assertEquals("access", response.accessToken());
        assertEquals("refresh", response.refreshToken());
    }

    @Test
    void refresh_shouldGenerateNewTokens_whenRefreshTokenValid() {
        String refreshToken = "validRefresh";
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

        RefreshToken storedToken = RefreshToken.builder()
                .user(user)
                .expiresAt(Instant.now().plusSeconds(60))
                .used(false)
                .build();

        when(jwtService.isRefreshToken(refreshToken)).thenReturn(true);
        when(refreshTokenService.useRefreshToken(refreshToken)).thenReturn(storedToken);
        when(jwtService.extractUsername(refreshToken)).thenReturn(PHONE);
        when(ssoUserService.findSsoUserByPhone(PHONE)).thenReturn(user);
        when(jwtService.generateAccessToken(any())).thenReturn("newAccess");
        when(jwtService.generateRefreshToken(any())).thenReturn("newRefresh");

        AuthResponse response = authService.refresh(request, DEVICE, IP);

        assertEquals("newAccess", response.accessToken());
        assertEquals("newRefresh", response.refreshToken());

        verify(refreshTokenService)
                .addRefreshToken(eq(user), eq("newRefresh"), eq(DEVICE), eq(IP));
    }

    @Test
    void refresh_shouldThrow_whenTokenIsNotRefresh() {
        String token = "badToken";
        RefreshTokenRequest request = new RefreshTokenRequest(token);

        when(jwtService.isRefreshToken(token)).thenReturn(false);

        assertThrows(IllegalRefreshTokenException.class,
                () -> authService.refresh(request, DEVICE, IP));

        verifyNoInteractions(refreshTokenService);
    }

    @Test
    void refresh_shouldThrow_whenTokenExpired() {
        String token = "expired";
        RefreshTokenRequest request = new RefreshTokenRequest(token);

        RefreshToken storedToken = RefreshToken.builder()
                .user(user)
                .expiresAt(Instant.now().minusSeconds(60))
                .used(false)
                .build();

        when(jwtService.isRefreshToken(token)).thenReturn(true);
        when(refreshTokenService.useRefreshToken(token)).thenReturn(storedToken);

        assertThrows(RefreshTokenExpiredException.class,
                () -> authService.refresh(request, DEVICE, IP));
    }

    @Test
    void logout_shouldDeleteToken_whenOwnerMatches() {
        String tokenValue = "refresh";
        RefreshTokenRequest request = new RefreshTokenRequest(tokenValue);

        RefreshToken token = RefreshToken.builder()
                .user(user)
                .build();

        when(refreshTokenService.findRefreshToken(tokenValue)).thenReturn(token);

        authService.logout(1L, request);

        verify(refreshTokenService).deleteToken(tokenValue);
    }

    @Test
    void logout_shouldForceDeleteAll_whenOwnerMismatch() {
        String tokenValue = "refresh";
        RefreshTokenRequest request = new RefreshTokenRequest(tokenValue);

        SsoUser anotherUser = SsoUser.builder().id(2L).build();

        RefreshToken token = RefreshToken.builder()
                .user(anotherUser)
                .build();

        when(refreshTokenService.findRefreshToken(tokenValue)).thenReturn(token);

        assertThrows(AuthorizationException.class,
                () -> authService.logout(1L, request));

        verify(refreshTokenService)
                .deleteAllByUserIdInNewTx(2L);
    }
}
