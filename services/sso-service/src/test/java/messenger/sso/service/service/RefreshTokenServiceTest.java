package messenger.sso.service.service;

import messenger.sso.service.domain.entity.RefreshToken;
import messenger.sso.service.domain.entity.SsoUser;
import messenger.sso.service.domain.repository.RefreshTokenRepository;
import messenger.sso.service.exception.*;
import messenger.sso.service.security.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private SsoUser user;

    @BeforeEach
    void setup() {
        user = SsoUser.builder()
                .id(1L)
                .phone("77777777")
                .username("user")
                .password("encoded")
                .build();
    }

    @Test
    void useRefreshToken_shouldReturnToken_whenFirstUse() {
        String token = "valid";

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .build();

        when(refreshTokenRepository.markAsUsed(token)).thenReturn(1);
        when(refreshTokenRepository.findByToken(token))
                .thenReturn(Optional.of(refreshToken));

        RefreshToken result = refreshTokenService.useRefreshToken(token);

        assertEquals(token, result.getToken());
    }

    @Test
    void useRefreshToken_shouldDeleteAllAndThrow_whenReuseDetected() {
        String token = "reused";

        RefreshToken stolenToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .build();

        when(refreshTokenRepository.markAsUsed(token)).thenReturn(0);
        when(refreshTokenRepository.findByToken(token))
                .thenReturn(Optional.of(stolenToken));

        when(applicationContext.getBean(RefreshTokenService.class))
                .thenReturn(refreshTokenService);

        assertThrows(RefreshTokenReuseException.class,
                () -> refreshTokenService.useRefreshToken(token));

        verify(refreshTokenRepository)
                .deleteAllByUserId(user.getId());
    }

    @Test
    void addRefreshToken_shouldSave_whenUnderLimit() {
        when(refreshTokenRepository.findAllByUserOrderByCreatedAtAsc(user))
                .thenReturn(List.of());

        when(jwtService.getRefreshTokenExpiration())
                .thenReturn(refreshTokenExpiration);

        refreshTokenService.addRefreshToken(user, "newToken", "Chrome", "127.0.0.1");

        verify(refreshTokenRepository).save(any(RefreshToken.class));
        verify(refreshTokenRepository, never()).delete(any());
    }

    @Test
    void addRefreshToken_shouldDeleteOldest_whenLimitExceeded() {
        RefreshToken oldest = RefreshToken.builder().token("old").build();

        List<RefreshToken> tokens = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tokens.add(oldest);
        }

        when(refreshTokenRepository.findAllByUserOrderByCreatedAtAsc(user))
                .thenReturn(tokens);

        when(jwtService.getRefreshTokenExpiration())
                .thenReturn(60000L);

        refreshTokenService.addRefreshToken(user, "newToken", "Chrome", "127.0.0.1");

        verify(refreshTokenRepository).delete(oldest);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void findRefreshToken_shouldReturn_whenExists() {
        String token = "valid";

        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .build();

        when(refreshTokenRepository.findByToken(token))
                .thenReturn(Optional.of(refreshToken));

        RefreshToken result = refreshTokenService.findRefreshToken(token);

        assertEquals(token, result.getToken());
    }

    @Test
    void findRefreshToken_shouldThrow_whenNotFound() {
        when(refreshTokenRepository.findByToken("bad"))
                .thenReturn(Optional.empty());

        assertThrows(RefreshTokenNotFoundException.class,
                () -> refreshTokenService.findRefreshToken("bad"));
    }

    @Test
    void deleteToken_shouldCallRepository() {
        refreshTokenService.deleteToken("token");

        verify(refreshTokenRepository).deleteToken("token");
    }

    @Test
    void deleteAllByUserId_shouldCallRepository() {
        refreshTokenService.deleteAllByUserId(1L);

        verify(refreshTokenRepository).deleteAllByUserId(1L);
    }
}
