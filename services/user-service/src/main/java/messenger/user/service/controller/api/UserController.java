package messenger.user.service.controller.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import messenger.user.service.dto.request.*;
import messenger.user.service.dto.response.UserResponse;
import messenger.user.service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> register(@RequestBody @Valid CreateUserRequest request) {
        UserResponse response = userService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    public ResponseEntity<UserResponse> getUser(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId
    ) {
        UserResponse response = userService.getUser(userId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/update/phone")
    public ResponseEntity<UserResponse> updatePhone(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid UpdatePhoneRequest request
    ) {
        UserResponse response = userService.updatePhone(userId, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/update/password")
    public ResponseEntity<UserResponse> updatePassword(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid UpdatePasswordRequest request
    ) {
        UserResponse response = userService.updatePassword(userId, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/update/username")
    public ResponseEntity<UserResponse> updateUsername(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid UpdateUsernameRequest request
    ) {
        UserResponse response = userService.updateUsername(userId, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/update/email")
    public ResponseEntity<UserResponse> updateEmail(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid UpdateEmailRequest request
    ) {
        UserResponse response = userService.updateEmail(userId, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}