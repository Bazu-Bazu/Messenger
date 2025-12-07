package messenger.sso.service.service;

import lombok.RequiredArgsConstructor;
import messenger.sso.service.dto.event.UserRegistrationEvent;
import messenger.sso.service.dto.event.UserUpdatingEvent;
import messenger.sso.service.entity.SsoUser;
import messenger.sso.service.exception.UserException;
import messenger.sso.service.repository.SsoUserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SsoUserService {

    private final SsoUserRepository ssoUserRepository;

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

}
