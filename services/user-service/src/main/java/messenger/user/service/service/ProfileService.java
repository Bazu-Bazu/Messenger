package messenger.user.service.service;

import lombok.RequiredArgsConstructor;
import messenger.user.service.dto.request.AddAvatarRequest;
import messenger.user.service.dto.request.UpdateProfileRequest;
import messenger.user.service.dto.response.ProfileResponse;
import messenger.user.service.domain.entity.Profile;
import messenger.user.service.domain.entity.User;
import messenger.user.service.exception.UserNotFoundException;
import messenger.user.service.domain.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    @Transactional
    @CacheEvict(value = "userProfiles", key = "#p0")
    public ProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with id %d not found", userId)
                ));

        user.getProfile().updateFrom(request);
        User savedUser = userRepository.save(user);

        return createProfileResponse(userId, savedUser.getProfile());
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
    public ProfileResponse addAvatarUrl(Long userId, AddAvatarRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with id %d not found", userId)
                ));

        user.getProfile().setAvatarUrl(request.url());
        userRepository.save(user);

        return createProfileResponse(userId, user.getProfile());
    }

    @Cacheable(value = "userProfiles", key = "#p0")
    public ProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with id %d not found", userId)
                ));

        return createProfileResponse(userId, user.getProfile());
    }
}
