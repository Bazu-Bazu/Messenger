package messenger.chat.service.service;

import dto.event.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import messenger.chat.service.domain.entity.User;
import messenger.chat.service.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
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
        int updated = userRepository.updateAvatarId(event.id(), event.avatarId());

        if (updated == 0) {
            log.warn("Avatar not changed for user {}", event.id());
        }
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByIds(List<Long> userIds) {
        return userRepository.findUsersByIds(userIds);
    }
}
