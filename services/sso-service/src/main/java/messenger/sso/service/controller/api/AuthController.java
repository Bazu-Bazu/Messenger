package messenger.sso.service.controller.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import messenger.sso.service.dto.request.RefreshTokenRequest;
import messenger.sso.service.dto.request.LoginRequest;
import messenger.sso.service.dto.response.AuthResponse;
import messenger.sso.service.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        String deviceInfo = httpRequest.getHeader("User-Agent");
        String ipAddress = httpRequest.getRemoteAddr();

        AuthResponse response = authService.login(request, deviceInfo, ipAddress);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestBody @Valid RefreshTokenRequest request,
            HttpServletRequest httpRequest
    ) {
        String deviceInfo = httpRequest.getHeader("User-Agent");
        String ipAddress = httpRequest.getRemoteAddr();

        AuthResponse response = authService.refresh(request, deviceInfo, ipAddress);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/logout")
    public ResponseEntity<Void> logout(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid RefreshTokenRequest request
    ) {
        authService.logout(userId, request);

        return ResponseEntity.noContent().build();
    }
}
