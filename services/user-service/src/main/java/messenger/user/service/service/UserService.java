package messenger.user.service.service;

import enums.UserUpdateType;
import lombok.RequiredArgsConstructor;
import messenger.user.service.dto.request.CreateUserRequest;
import messenger.user.service.dto.response.UserResponse;
import messenger.user.service.domain.entity.User;
import messenger.user.service.exception.UserException;
import messenger.user.service.domain.repository.UserRepository;
import messenger.user.service.client.kafka.UserEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        userEventPublisher.sendUserRegistrationToKafka(savedUser);

        return createUserResponse(savedUser);
    }

    private User createUser(CreateUserRequest request) {
        return User.builder()
                .username(request.username())
                .phone(request.phone())
                .password(passwordEncoder.encode(request.password()))
                .build();
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

    public UserResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(
                        String.format("User with id %d not found", userId)
                ));

        return createUserResponse(user);
    }

    public UserResponse updateUser(Long userId, String updatedField, UserUpdateType type) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(
                        String.format("User with id %d not found", userId)
                ));

        switch (type) {
            case EMAIL -> user.setEmail(updatedField);
            case PASSWORD -> user.setPassword(passwordEncoder.encode(updatedField));
            case USERNAME -> user.setUsername(updatedField);
            case PHONE -> user.setPhone(updatedField);
        }

        User savedUser = userRepository.save(user);
        userEventPublisher.sendUserUpdatingToKafka(savedUser, type);
        return createUserResponse(savedUser);
    }

}
