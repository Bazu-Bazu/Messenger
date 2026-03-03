package messenger.sso.service.service;

import dto.event.UserEvent;
import lombok.RequiredArgsConstructor;
import messenger.sso.service.domain.entity.SsoUser;
import messenger.sso.service.exception.SsoUserNotFoundException;
import messenger.sso.service.domain.repository.SsoUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SsoUserService {

    private final SsoUserRepository ssoUserRepository;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void createSsoUser(UserEvent event) {
        SsoUser newUser = SsoUser.builder()
                .id(event.id())
                .username(event.username())
                .phone(event.phone())
                .email(event.email())
                .password(event.password())
                .build();

        ssoUserRepository.save(newUser);
    }

    @Transactional
    public void updatePhone(UserEvent event) {
        SsoUser user = findSsoUserById(event.id());

        user.setPhone(event.phone());

        refreshTokenService.deleteAllByUserId(event.id());
    }

    @Transactional
    public void updatePassword(UserEvent event) {
        SsoUser user = findSsoUserById(event.id());

        user.setPassword(event.password());

        refreshTokenService.deleteAllByUserId(event.id());
    }

    @Transactional
    public void updateEmail(UserEvent event) {
        SsoUser user = findSsoUserById(event.id());

        user.setEmail(event.email());
    }

    @Transactional
    public void updateUsername(UserEvent event) {
        SsoUser user = findSsoUserById(event.id());

        user.setUsername(event.username());
    }

    @Transactional(readOnly = true)
    public SsoUser findSsoUserById(Long userId) {
        return ssoUserRepository.findById(userId)
                .orElseThrow(() -> new SsoUserNotFoundException(
                        String.format("User %d not found", userId)
                ));
    }

    @Transactional(readOnly = true)
    public SsoUser findSsoUserByPhone(String phone) {
        return ssoUserRepository.findByPhone(phone)
                .orElseThrow(() -> new SsoUserNotFoundException(
                        String.format("User with phone %s not found", phone)
                ));
    }
}
