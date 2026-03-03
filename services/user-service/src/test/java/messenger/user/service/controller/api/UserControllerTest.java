package messenger.user.service.controller.api;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import messenger.user.service.domain.repository.UserRepository;
import messenger.user.service.dto.request.*;
import messenger.user.service.dto.response.UserResponse;
import messenger.user.service.exception.mapper.ErrorMapper;
import messenger.user.service.service.UserService;
import messenger.user.service.validation.validator.UniqueEmailValidator;
import messenger.user.service.validation.validator.UniquePhoneValidator;
import messenger.user.service.validation.validator.UniqueUsernameValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private ErrorMapper errorMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UniqueUsernameValidator uniqueUsernameValidator;

    @MockBean
    private UniquePhoneValidator uniquePhoneValidator;

    @MockBean
    private UniqueEmailValidator uniqueEmailValidator;

    @Test
    void register_shouldReturnCreatedUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest(
                "john_doe",
                "+1234567890",
                "Password1"
        );

        UserResponse response = UserResponse.builder()
                .id(1L)
                .username("john_doe")
                .phone("+1234567890")
                .build();

        when(userService.registerUser(any(CreateUserRequest.class))).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.phone").value("+1234567890"));
    }

    @Test
    void getUser_shouldReturnUser() throws Exception {
        UserResponse response = UserResponse.builder()
                .id(1L)
                .username("john_doe")
                .phone("+1234567890")
                .build();

        when(userService.getUser(1L)).thenReturn(response);

        mockMvc.perform(get("/users")
                        .header("X-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("john_doe"));
    }

    @Test
    void updatePhone_shouldReturnUpdatedUser() throws Exception {
        UpdatePhoneRequest request = new UpdatePhoneRequest("+1987654321");
        UserResponse response = UserResponse.builder()
                .id(1L)
                .phone("+1987654321")
                .build();

        when(userService.updatePhone(eq(1L), any(UpdatePhoneRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/users/update/phone")
                        .header("X-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("+1987654321"));
    }

    @Test
    void updatePassword_shouldReturnUpdatedUser() throws Exception {
        UpdatePasswordRequest request = new UpdatePasswordRequest("NewPassword1");
        UserResponse response = UserResponse.builder()
                .id(1L)
                .build();

        when(userService.updatePassword(eq(1L), any(UpdatePasswordRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/users/update/password")
                        .header("X-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateUsername_shouldReturnUpdatedUser() throws Exception {
        UpdateUsernameRequest request = new UpdateUsernameRequest("jane_doe");
        UserResponse response = UserResponse.builder()
                .id(1L)
                .username("jane_doe")
                .build();

        when(userService.updateUsername(eq(1L), any(UpdateUsernameRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/users/update/username")
                        .header("X-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("jane_doe"));
    }

    @Test
    void updateEmail_shouldReturnUpdatedUser() throws Exception {
        UpdateEmailRequest request = new UpdateEmailRequest("jane@example.com");
        UserResponse response = UserResponse.builder()
                .id(1L)
                .email("jane@example.com")
                .build();

        when(userService.updateEmail(eq(1L), any(UpdateEmailRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/users/update/email")
                        .header("X-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("jane@example.com"));
    }
}
