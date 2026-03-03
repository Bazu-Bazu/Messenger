package messenger.sso.service.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import messenger.sso.service.dto.request.LoginRequest;
import messenger.sso.service.dto.request.RefreshTokenRequest;
import messenger.sso.service.dto.response.AuthResponse;
import messenger.sso.service.exception.mapper.ErrorMapper;
import messenger.sso.service.security.jwt.JwtFilter;
import messenger.sso.service.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private ErrorMapper errorMapper;

    @Test
    void login_shouldReturnAuthResponse() throws Exception {
        LoginRequest request = new LoginRequest(
                "79999999999",
                "password"
        );

        AuthResponse response = AuthResponse.builder()
                .userId(1L)
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();

        when(authService.login(
                any(LoginRequest.class),
                anyString(),
                anyString()
        )).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                        .header("User-Agent", "Chrome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
    }

    @Test
    void refresh_shouldReturnNewTokens() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest("refreshToken");

        AuthResponse response = AuthResponse.builder()
                .userId(1L)
                .accessToken("newAccess")
                .refreshToken("newRefresh")
                .build();

        when(authService.refresh(
                any(RefreshTokenRequest.class),
                anyString(),
                anyString()
        )).thenReturn(response);

        mockMvc.perform(post("/auth/refresh")
                        .header("User-Agent", "Chrome")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("newAccess"))
                .andExpect(jsonPath("$.refreshToken").value("newRefresh"))
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void logout_shouldReturnNoContent() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest("refreshToken");

        mockMvc.perform(patch("/auth/logout")
                        .header("X-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        verify(authService).logout(eq(1L), any(RefreshTokenRequest.class));
    }
}
