package messenger.sso.service.service;

import lombok.RequiredArgsConstructor;
import messenger.sso.service.domain.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;



}
