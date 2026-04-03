package messenger.user.service.service;

import messenger.user.service.domain.entity.Profile;
import messenger.user.service.domain.entity.User;
import messenger.user.service.domain.enums.UserStatus;
import messenger.user.service.domain.repository.UserRepository;
import messenger.user.service.dto.UserExistenceDto;
import messenger.user.service.dto.UserInfoDto;
import messenger.user.service.dto.request.CreateUserRequest;
import messenger.user.service.dto.request.UpdatePasswordRequest;
import messenger.user.service.dto.request.UpdatePhoneRequest;
import messenger.user.service.dto.response.UserResponse;
import messenger.user.service.service.event.UserEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserEventPublisher userEventPublisher;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_shouldSaveUserAndPublishEvent() {
        CreateUserRequest request = new CreateUserRequest("john", "12345", "password");
        User user = User.builder()
                .id(1L)
                .username("john")
                .phone("12345")
                .password("encoded")
                .build();

        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.registerUser(request);

        assertEquals(1L, response.getId());
        assertEquals("john", response.getUsername());
        verify(userEventPublisher).publishUserRegistration(user);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUser_shouldReturnUserResponse() {
        User user = User.builder()
                .id(1L).username("john")
                .phone("12345")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUser(1L);

        assertEquals("john", response.getUsername());
    }

    @Test
    void updatePhone_shouldUpdateAndPublishEvent() {
        User user = User.builder()
                .id(1L).username("john")
                .phone("12345")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UpdatePhoneRequest request = new UpdatePhoneRequest("67890");
        UserResponse response = userService.updatePhone(1L, request);

        assertEquals("67890", user.getPhone());
        verify(userEventPublisher).publishUserPhoneChanged(user);
    }

    @Test
    void updatePassword_shouldEncodeAndPublishEvent() {
        User user = User.builder()
                .id(1L).username("john")
                .password("old")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpass")).thenReturn("encodedNew");

        UpdatePasswordRequest request = new UpdatePasswordRequest("newpass");
        userService.updatePassword(1L, request);

        assertEquals("encodedNew", user.getPassword());
        verify(userEventPublisher).publishUserPasswordChanged(user);
    }

    @Test
    void validateUserExist_shouldReturnCorrectDto() {
        User user = User.builder().id(1L).status(UserStatus.ACTIVE).build();
        when(userRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(user));

        List<UserExistenceDto> result = userService.validateUserExist(List.of(1L, 2L));

        assertEquals(2, result.size());
        assertTrue(result.get(0).exists());
        assertTrue(result.get(0).isActive());
        assertFalse(result.get(1).exists());
    }

    @Test
    void getUsersInfo_shouldReturnUserInfoDtos() {
        Profile profile = new Profile();
        profile.setAvatarId(10L);

        User user = User.builder()
                .id(1L)
                .username("john")
                .profile(profile)
                .build();

        when(userRepository.findAllById(List.of(1L))).thenReturn(List.of(user));

        List<UserInfoDto> result = userService.getUsersInfo(List.of(1L));

        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).avatarId());
    }
}
