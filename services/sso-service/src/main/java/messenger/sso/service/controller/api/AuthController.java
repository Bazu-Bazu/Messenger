package messenger.sso.service.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import messenger.sso.service.dto.request.RefreshTokenRequest;
import messenger.sso.service.dto.request.SignInRequest;
import messenger.sso.service.dto.response.AuthResponse;
import messenger.sso.service.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(
            @RequestBody @Valid SignInRequest request,
            HttpServletRequest httpRequest
    ) {
        String deviceInfo = httpRequest.getHeader("User-Agent");
        String ipAddress = httpRequest.getRemoteAddr();

        AuthResponse response = authService.signIn(request, deviceInfo, ipAddress);

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @RequestBody @Valid RefreshTokenRequest request,
            HttpServletRequest httpRequest
    ) {
        String deviceInfo = httpRequest.getHeader("User-Agent");
        String ipAddress = httpRequest.getRemoteAddr();

        AuthResponse response = authService.refresh(request, deviceInfo, ipAddress);

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody @Valid RefreshTokenRequest request) {
        authService.logout(request);

        return ResponseEntity.status(200).body(null);
    }

}
