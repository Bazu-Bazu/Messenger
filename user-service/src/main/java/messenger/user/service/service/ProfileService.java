package messenger.user.service.service;

import lombok.RequiredArgsConstructor;
import messenger.user.service.dto.request.UpdateProfileRequest;
import messenger.user.service.dto.response.ProfileResponse;
import messenger.user.service.embeddable.Profile;
import messenger.user.service.entity.User;
import messenger.user.service.exception.UserException;
import messenger.user.service.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    @Transactional
    public ProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(
                        String.format("User with id %d not found", userId)
                ));

        user.getProfile().updateFrom(request);
        userRepository.save(user);

        return createProfileResponse(userId, user.getProfile());
    }

    private ProfileResponse createProfileResponse(Long userId, Profile profile) {
        return ProfileResponse.builder()
                .userId(userId)
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .birthDate(profile.getBirthDate())
                .age(profile.getAge())
                .gender(profile.getGender())
                .avatarUrl(profile.getAvatarUrl())
                .bio(profile.getBio())
                .build();
    }

    @Transactional
    public ProfileResponse addAvatarUrl(Long userId, String url) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(
                        String.format("User with id %d not found", userId)
                ));

        user.getProfile().setAvatarUrl(url);
        userRepository.save(user);

        return createProfileResponse(userId, user.getProfile());
    }

    public ProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(
                        String.format("User with id %d not found", userId)
                ));

        return createProfileResponse(userId, user.getProfile());
    }

}
