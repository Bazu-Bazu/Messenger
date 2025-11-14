package messenger.user.service.validation;

import lombok.RequiredArgsConstructor;
import messenger.user.service.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniquenessChecker {

    private final UserRepository userRepository;

    public boolean isUsernameUnique(String username) {
        return !userRepository.existsByUsername(username);
    }

    public boolean isPhoneUnique(String phone) {
        return !userRepository.existsByPhone(phone);
    }

    public boolean isEmailUnique(String email) {
        return !userRepository.existsByEmail(email);
    }

}
