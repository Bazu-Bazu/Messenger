package messenger.chat.service.service;

import dto.event.UserEvent;
import lombok.RequiredArgsConstructor;
import messenger.chat.service.domain.entity.User;
import messenger.chat.service.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void createUser(UserEvent event) {
        User user = User.builder()
                .userId(event.id())
                .username(event.username())
                .avatarId(event.avatarId())
                .build();

        userRepository.save(user);
    }

    @Transactional
    public void updateAvatar(UserEvent event) {
        userRepository.updateAvatarId(event.id(), event.avatarId());
    }
}
