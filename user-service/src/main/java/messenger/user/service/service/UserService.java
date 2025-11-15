package messenger.user.service.service;

import lombok.RequiredArgsConstructor;
import messenger.user.service.dto.request.CreateUserRequest;
import messenger.user.service.dto.response.UserResponse;
import messenger.user.service.entity.User;
import messenger.user.service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse registerUser(CreateUserRequest request) {
        User newUser = createUser(request);
        userRepository.save(newUser);

        return createUserResponse(newUser);
    }

    private User createUser(CreateUserRequest request) {
        return User.builder()
                .username(request.username())
                .phone(request.phone())
                .password(passwordEncoder.encode(request.password()))
                .createdAt(Instant.now())
                .build();
    }

    private UserResponse createUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .phone(user.getPhone())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}
