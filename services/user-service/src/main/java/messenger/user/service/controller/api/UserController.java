package messenger.user.service.controller.api;

import enums.UserUpdateType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import messenger.user.service.dto.request.*;
import messenger.user.service.dto.response.UserResponse;
import messenger.user.service.service.UserService;
import messenger.user.service.service.UserValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserValidationService userValidationService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody @Valid CreateUserRequest request) {
        userValidationService.validateUserRegistration(request.username(), request.phone(), request.password());
        UserResponse response = userService.registerUser(request);

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyTools(@RequestHeader("X-User-Id") Long userId) {
        UserResponse response = userService.getUser(userId);

        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/update/phone")
    public ResponseEntity<?> updatePhone(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid UpdatePhoneRequest request
    ) {
        userValidationService.validatePhone(request.phone());
        UserResponse response = userService.updateUser(userId, request.phone(), UserUpdateType.PHONE);

        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/update/password")
    public ResponseEntity<?> updatePassword(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid UpdatePasswordRequest request
    ) {
        userValidationService.validatePassword(request.password());
        UserResponse response = userService.updateUser(userId, request.password(), UserUpdateType.PASSWORD);

        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/update/username")
    public ResponseEntity<?> updateUsername(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid UpdateUsernameRequest request
    ) {
        userValidationService.validateUsername(request.username());
        UserResponse response = userService.updateUser(userId, request.username(), UserUpdateType.USERNAME);

        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/update/email")
    public ResponseEntity<?> updateEmail(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid UpdateEmailRequest request
    ) {
        userValidationService.validateEmail(request.email());
        UserResponse response = userService.updateUser(userId, request.email(), UserUpdateType.EMAIL);

        return ResponseEntity.status(200).body(response);
    }

}