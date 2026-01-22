package messenger.user.service.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import messenger.user.service.dto.request.*;
import messenger.user.service.dto.response.UserResponse;
import messenger.user.service.service.UserService;
import messenger.user.service.service.UserValidationService;
import messenger.user.service.validation.UserUpdateType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;
    private final UserValidationService userValidationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid CreateUserRequest request) {
        log.info("Registration attempt for phone: {}", request.phone());

        userValidationService.validateUserRegistration(request.username(), request.phone(), request.password());
        UserResponse response = userService.registerUser(request);

        log.info("User with phone {} registered successfully", response.phone());

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/my-tools")
    public ResponseEntity<?> getMyTools(@RequestParam("userId") Long userId) {
        log.info("Getting tools attempt for user with id {}", userId);

        UserResponse response = userService.getUser(userId);

        log.info("Got tools with user id {} successfully", userId);

        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/update/phone")
    public ResponseEntity<?> updatePhone(
            @RequestParam("userId") @Valid Long userId,
            @RequestBody @Valid UpdatePhoneRequest request
    ) {
        log.info("Updating phone attempt for userId: {}", userId);

        userValidationService.validatePhone(request.phone());
        UserResponse response = userService.updateUser(userId, request.phone(), UserUpdateType.PHONE);

        log.info("User with id {} updated phone successfully", userId);

        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/update/password")
    public ResponseEntity<?> updatePassword(
            @RequestParam("userId") @Valid Long userId,
            @RequestBody @Valid UpdatePasswordRequest request
    ) {
        log.info("Updating password attempt for userId: {}", userId);

        userValidationService.validatePassword(request.password());
        UserResponse response = userService.updateUser(userId, request.password(), UserUpdateType.PASSWORD);

        log.info("User with id {} updated password successfully", userId);

        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/update/username")
    public ResponseEntity<?> updateUsername(
            @RequestParam("userId") @Valid Long userId,
            @RequestBody @Valid UpdateUsernameRequest request
    ) {
        log.info("Updating username attempt for userId: {}", userId);

        userValidationService.validateUsername(request.username());
        UserResponse response = userService.updateUser(userId, request.username(), UserUpdateType.USERNAME);

        log.info("User with id {} updated username successfully", userId);

        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/update/email")
    public ResponseEntity<?> updateEmail(
            @RequestParam("userId") @Valid Long userId,
            @RequestBody @Valid UpdateEmailRequest request
    ) {
        log.info("Updating email attempt for userId: {}", userId);

        userValidationService.validateEmail(request.email());
        UserResponse response = userService.updateUser(userId, request.email(), UserUpdateType.EMAIL);

        log.info("User with id {} updated email successfully", userId);

        return ResponseEntity.status(200).body(response);
    }

}