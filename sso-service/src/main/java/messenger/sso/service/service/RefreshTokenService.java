package messenger.sso.service.service;

import lombok.RequiredArgsConstructor;
import messenger.sso.service.entity.SsoUser;
import messenger.sso.service.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;



}
