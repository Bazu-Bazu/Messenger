package com.burkina.messenger.service;

import com.burkina.common.dto.event.user.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import com.burkina.messenger.domain.entity.User;
import com.burkina.messenger.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void createUser(UserRegisteredEvent event) {
        User user = User.builder()
                .userId(event.userId())
                .username(event.username())
                .build();

        userRepository.save(user);
    }

//    @Transactional
//    public void updateAvatar(UserUpdatedEvent event) {
//        int updated = userRepository.updateAvatarId(event.userId(), event.avatarId());
//
//        if (updated == 0) {
//            log.warn("Avatar not changed for user {}", event.userId());
//        }
//    }

    @Transactional(readOnly = true)
    public List<User> getUsersByIds(List<Long> userIds) {
        return userRepository.findUsersByIds(userIds);
    }
}
