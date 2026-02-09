package messenger.sso.service.service;

import dto.event.UserUpdatingEvent;
import lombok.RequiredArgsConstructor;
import dto.event.UserRegistrationEvent;
import messenger.sso.service.domain.entity.SsoUser;
import messenger.sso.service.exception.SsoUserException;
import messenger.sso.service.exception.UserException;
import messenger.sso.service.domain.repository.SsoUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SsoUserService {

    private final SsoUserRepository ssoUserRepository;

    @Transactional
    public void createSsoUser(UserRegistrationEvent event) {
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
    public void updateSsoUser(UserUpdatingEvent event) {
        SsoUser user = ssoUserRepository.findById(event.id())
                .orElseThrow(() -> new UserException(
                        String.format("User with id %d not found", event.id())
                ));

        switch (event.type()) {
            case EMAIL -> user.setEmail(event.updatedField());
            case PASSWORD -> user.setPassword(event.updatedField());
            case USERNAME -> user.setUsername(event.updatedField());
            case PHONE -> user.setPhone(event.updatedField());
        }

        ssoUserRepository.save(user);
    }

    public SsoUser findSsoUserByPhone(String phone) {
        return ssoUserRepository.findByPhone(phone)
                .orElseThrow(() -> new SsoUserException(
                        String.format("User with phone %s not found", phone)
                ));
    }

}
