package messenger.user.service.service;

import lombok.RequiredArgsConstructor;
import messenger.user.service.dto.request.CreateUserRequest;
import messenger.user.service.dto.response.UserResponse;
import messenger.user.service.entity.User;
import messenger.user.service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserValidationService userValidationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(CreateUserRequest request) {
        userValidationService.validationRegisterUser(request.username(), request.phone(), request.password());

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
                .build();
    }

}
