package messenger.sso.service.service;

import lombok.RequiredArgsConstructor;
import messenger.sso.service.dto.event.UserEvent;
import messenger.sso.service.entity.SsoUser;
import messenger.sso.service.repository.SsoUserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SsoUserService {

    private final SsoUserRepository ssoUserRepository;

    public void createSsoUser(UserEvent event) {
        SsoUser newUser = SsoUser.builder()
                .id(event.id())
                .username(event.userName())
                .phone(event.phone())
                .email(event.email())
                .password(event.password())
                .build();

        ssoUserRepository.save(newUser);
    }

}
