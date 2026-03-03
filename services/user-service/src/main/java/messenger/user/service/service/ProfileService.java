package messenger.user.service.service;

import lombok.RequiredArgsConstructor;
import messenger.user.service.dto.request.AddAvatarRequest;
import messenger.user.service.dto.request.UpdateProfileRequest;
import messenger.user.service.dto.response.ProfileResponse;
import messenger.user.service.domain.entity.Profile;
import messenger.user.service.domain.entity.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserService userService;

    @Transactional
    @CacheEvict(value = "userProfiles", key = "#p0")
    public ProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userService.findUserById(userId);

        user.getProfile().updateFrom(request);

        return createProfileResponse(userId, user);
    }

    @Transactional
    @CacheEvict(value = "userProfiles", key = "#p0")
    public ProfileResponse addAvatarUrl(Long userId, AddAvatarRequest request) {
        User user = userService.findUserById(userId);

        user.getProfile().setAvatarUrl(request.url());

        return createProfileResponse(userId, user);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "userProfiles", key = "#p0")
    public ProfileResponse getProfile(Long userId) {
        User user = userService.findUserById(userId);

        return createProfileResponse(userId, user);
    }

    private ProfileResponse createProfileResponse(Long userId, User user) {
        Profile profile = user.getProfile();

        return ProfileResponse.builder()
                .userId(userId)
                .username(user.getUsername())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .birthDate(profile.getBirthDate())
                .age(profile.getAge())
                .gender(profile.getGender())
                .avatarUrl(profile.getAvatarUrl())
                .bio(profile.getBio())
                .build();
    }
}
