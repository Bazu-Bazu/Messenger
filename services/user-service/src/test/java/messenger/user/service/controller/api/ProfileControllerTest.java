package messenger.user.service.controller.api;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import messenger.user.service.domain.enums.Gender;
import messenger.user.service.dto.request.AddAvatarRequest;
import messenger.user.service.dto.request.UpdateProfileRequest;
import messenger.user.service.dto.response.ProfileResponse;
import messenger.user.service.exception.mapper.ErrorMapper;
import messenger.user.service.service.ProfileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfileService profileService;

    @MockBean
    private ErrorMapper errorMapper;

    @Test
    void updateProfile_shouldReturnUpdatedProfile() throws Exception {
        UpdateProfileRequest request = UpdateProfileRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .gender(Gender.MALE)
                .bio("Hello")
                .birthDate(null)
                .build();

        ProfileResponse response = ProfileResponse.builder()
                .userId(1L)
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .gender(Gender.MALE)
                .bio("Hello")
                .build();

        when(profileService.updateProfile(eq(1L), any(UpdateProfileRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/profile")
                        .header("X-User-Id", String.valueOf(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john"))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.bio").value("Hello"));
    }

    @Test
    void addAvatarUrl_shouldReturnProfileWithAvatar() throws Exception {
        AddAvatarRequest request = new AddAvatarRequest(10L);

        ProfileResponse response = ProfileResponse.builder()
                .userId(1L)
                .username("john")
                .avatarId(10L)
                .build();

        when(profileService.addAvatar(eq(1L), any(AddAvatarRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/profile/avatar")
                        .header("X-User-Id", String.valueOf(1L))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avatarId").value(10L))
                .andExpect(jsonPath("$.username").value("john"));
    }

    @Test
    void getProfile_shouldReturnProfileByUserId() throws Exception {
        ProfileResponse response = ProfileResponse.builder()
                .userId(2L)
                .username("jane")
                .build();

        when(profileService.getProfile(2L)).thenReturn(response);

        mockMvc.perform(get("/profile/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.username").value("jane"));
    }

    @Test
    void getMyProfile_shouldReturnProfileByHeader() throws Exception {
        ProfileResponse response = ProfileResponse.builder()
                .userId(1L)
                .username("john")
                .build();

        when(profileService.getProfile(1L)).thenReturn(response);

        mockMvc.perform(get("/profile")
                        .header("X-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.username").value("john"));
    }
}