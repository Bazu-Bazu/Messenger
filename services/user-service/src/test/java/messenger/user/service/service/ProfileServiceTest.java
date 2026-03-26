package messenger.user.service.service;

import messenger.user.service.domain.entity.Profile;
import messenger.user.service.domain.entity.User;
import messenger.user.service.domain.enums.Gender;
import messenger.user.service.dto.request.AddAvatarRequest;
import messenger.user.service.dto.request.UpdateProfileRequest;
import messenger.user.service.dto.response.ProfileResponse;
import messenger.user.service.service.event.UserEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private UserEventPublisher userEventPublisher;

    @InjectMocks
    private ProfileService profileService;

    @Test
    void addAvatarUrl_shouldSetAvatarAndReturnResponse() {
        Profile profile = new Profile();
        User user = User.builder().id(1L).username("john").profile(profile).build();

        when(userService.findUserById(1L)).thenReturn(user);

        AddAvatarRequest request = new AddAvatarRequest(10L);
        ProfileResponse response = profileService.addAvatar(1L, request);

        assertEquals(10L, user.getProfile().getAvatarId());
        assertEquals(10L, response.getAvatarId());
        assertEquals("john", response.getUsername());
        verify(userEventPublisher).publishUserAvatarChanged(user);
    }

    @Test
    void getProfile_shouldReturnProfileResponse() {
        Profile profile = new Profile();
        profile.setFirstName("John");
        profile.setLastName("Doe");
        profile.setAvatarId(10L);
        profile.setBirthDate(LocalDate.of(1990, 1, 1));
        profile.setGender(Gender.MALE);
        profile.setBio("Hello world");

        User user = User.builder().id(1L).username("john").profile(profile).build();
        when(userService.findUserById(1L)).thenReturn(user);

        ProfileResponse response = profileService.getProfile(1L);

        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals(10L, response.getAvatarId());
        assertEquals(Gender.MALE, response.getGender());
        assertEquals("Hello world", response.getBio());
    }

    @Test
    void updateProfile_shouldUpdateFieldsFromRequest() {
        Profile profile = new Profile();
        User user = User.builder()
                .id(1L)
                .username("john")
                .profile(profile)
                .build();

        when(userService.findUserById(1L)).thenReturn(user);

        UpdateProfileRequest request = UpdateProfileRequest.builder()
                .firstName("Jane")
                .lastName("Smith")
                .birthDate(LocalDate.of(2000, 5, 15))
                .gender(Gender.FEMALE)
                .bio("New bio")
                .build();

        ProfileResponse response = profileService.updateProfile(1L, request);

        assertEquals("Jane", user.getProfile().getFirstName());
        assertEquals("Smith", user.getProfile().getLastName());
        assertEquals(LocalDate.of(2000, 5, 15), user.getProfile().getBirthDate());
        assertEquals(Gender.FEMALE, user.getProfile().getGender());
        assertEquals("New bio", user.getProfile().getBio());

        assertEquals("Jane", response.getFirstName());
        assertEquals("Smith", response.getLastName());
        assertEquals(Gender.FEMALE, response.getGender());
        assertEquals("New bio", response.getBio());
    }
}
