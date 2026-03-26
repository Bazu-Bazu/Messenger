package messenger.user.service.service;

import lombok.RequiredArgsConstructor;
import messenger.user.service.domain.enums.UserStatus;
import messenger.user.service.dto.UserExistenceDto;
import messenger.user.service.dto.UserInfoDto;
import messenger.user.service.dto.request.*;
import messenger.user.service.dto.response.UserResponse;
import messenger.user.service.domain.entity.User;
import messenger.user.service.exception.UserNotFoundException;
import messenger.user.service.domain.repository.UserRepository;
import messenger.user.service.service.event.UserEventPublisher;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEventPublisher userEventPublisher;

    @Transactional
    public UserResponse registerUser(CreateUserRequest request) {
        User newUser = createUser(request);
        User savedUser = userRepository.save(newUser);

        userEventPublisher.publishUserRegistration(savedUser);

        return createUserResponse(savedUser);
    }

    private User createUser(CreateUserRequest request) {
        return User.builder()
                .username(request.username())
                .phone(request.phone())
                .password(passwordEncoder.encode(request.password()))
                .build();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#p0")
    public UserResponse getUser(Long userId) {
        User user = findUserById(userId);

        return createUserResponse(user);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#p0")
    public UserResponse updatePhone(Long userId, UpdatePhoneRequest request) {
        User user = findUserById(userId);
        user.setPhone(request.phone());

        userEventPublisher.publishUserPhoneChanged(user);

        return createUserResponse(user);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#p0")
    public UserResponse updatePassword(Long userId, UpdatePasswordRequest request) {
        User user = findUserById(userId);
        user.setPassword(passwordEncoder.encode(request.password()));

        userEventPublisher.publishUserPasswordChanged(user);

        return createUserResponse(user);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#p0")
    public UserResponse updateUsername(Long userId, UpdateUsernameRequest request) {
        User user = findUserById(userId);
        user.setUsername(request.username());

        userEventPublisher.publishUserUsernameChanged(user);

        return createUserResponse(user);
    }

    @Transactional
    @CacheEvict(value = "users", key = "#p0")
    public UserResponse updateEmail(Long userId, UpdateEmailRequest request) {
        User user = findUserById(userId);
        user.setEmail(request.email());

        userEventPublisher.publishUserEmailChanged(user);

        return createUserResponse(user);
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User %d not found", userId)
                ));
    }

    private UserResponse createUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phone(user.getPhone())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .status(user.getStatus())
                .build();
    }

    @Transactional(readOnly = true)
    public List<UserExistenceDto> validateUserExist(List<Long> userIds) {
        List<User> users = userRepository.findAllById(userIds);

        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        Function.identity()
                ));

        return userIds.stream()
                .map(userId -> {
                    User user = userMap.get(userId);

                    boolean exists = user != null;
                    boolean isActive = exists && user.getStatus().equals(UserStatus.ACTIVE);

                    return UserExistenceDto.builder()
                            .userId(userId)
                            .exists(exists)
                            .isActive(isActive)
                            .build();
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserInfoDto> getUsersInfo(List<Long> userIds) {
        List<User> users = userRepository.findAllById(userIds);

        return users.stream()
                .map(user -> UserInfoDto.builder()
                        .userId(user.getId())
                        .username(user.getUsername())
                        .avatarId(user.getProfile().getAvatarId() != null ? user.getProfile().getAvatarId() : 0)
                        .build()
                )
                .toList();
    }
}
