package messenger.user.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import messenger.user.service.dto.request.*;
import messenger.user.service.dto.response.UserResponse;
import messenger.user.service.exception.UserException;
import messenger.user.service.exception.ValidationException;
import messenger.user.service.service.UserService;
import messenger.user.service.service.UserValidationService;
import messenger.user.service.validation.UserUpdateType;
import messenger.user.service.validation.ValidationType;
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
        try {
            log.info("Registration attempt for phone: {}", request.phone());

            userValidationService.validateUserRegistration(request.username(), request.phone(), request.password());
            UserResponse response = userService.registerUser(request);

            log.info("User with phone {} registered successfully", response.phone());

            return ResponseEntity.status(201).body(response);
        } catch (ValidationException e) {
            log.warn("Registration with phone {} validation failed: {}", request.phone(), e.getMessage());

            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            log.warn("Unexpected error during registration with phone {}. Error: {}",
                    request.phone(), e.getMessage());

            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/my-tools")
    public ResponseEntity<?> getMyTools(@RequestParam Long userId) {
        try {
            log.info("Getting tools attempt for user with id {}", userId);

            UserResponse response = userService.getUser(userId);

            log.info("Got tools with user id {} successfully", userId);

            return ResponseEntity.status(200).body(response);
        } catch (UserException e) {
            log.warn("User with id {} not found. Error: {}", userId, e.getMessage());

            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            log.warn("Unexpected error during getting tools with userId {}. Error: {}",
                    userId, e.getMessage());

            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/update/phone")
    public ResponseEntity<?> updatePhone(
            @RequestParam @Valid Long userId,
            @RequestBody @Valid UpdatePhoneRequest request
    ) {
        try {
            log.info("Updating phone attempt for userId: {}", userId);

            userValidationService.validatePhone(request.phone());
            UserResponse response = userService.updateUser(userId, request.phone(), UserUpdateType.PHONE);

            log.info("User with id {} updated phone successfully", userId);

            return ResponseEntity.status(200).body(response);
        } catch (ValidationException e) {
            log.warn("Updating phone with userId {} validation failed: {}", userId, e.getMessage());

            return ResponseEntity.status(400).body(e.getMessage());
        } catch (UserException e) {
            log.warn("User with id {} not found. Error: {}", userId, e.getMessage());

            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            log.warn("Unexpected error during updating phone with userId {}. Error: {}",
                    userId, e.getMessage());

            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/update/password")
    public ResponseEntity<?> updatePassword(
            @RequestParam @Valid Long userId,
            @RequestBody @Valid UpdatePasswordRequest request
    ) {
        try {
            log.info("Updating password attempt for userId: {}", userId);

            userValidationService.validatePassword(request.password());
            UserResponse response = userService.updateUser(userId, request.password(), UserUpdateType.PASSWORD);

            log.info("User with id {} updated password successfully", userId);

            return ResponseEntity.status(200).body(response);
        } catch (ValidationException e) {
            log.warn("Updating password with userId {} validation failed: {}", userId, e.getMessage());

            return ResponseEntity.status(400).body(e.getMessage());
        } catch (UserException e) {
            log.warn("User with id {} not found. Error: {}", userId, e.getMessage());

            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            log.warn("Unexpected error during updating password with userId {}. Error: {}",
                    userId, e.getMessage());

            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/update/username")
    public ResponseEntity<?> updateUsername(
            @RequestParam @Valid Long userId,
            @RequestBody @Valid UpdateUsernameRequest request
    ) {
        try {
            log.info("Updating username attempt for userId: {}", userId);

            userValidationService.validateUsername(request.username());
            UserResponse response = userService.updateUser(userId, request.username(), UserUpdateType.USERNAME);

            log.info("User with id {} updated username successfully", userId);

            return ResponseEntity.status(200).body(response);
        } catch (ValidationException e) {
            log.warn("Updating username with userId {} validation failed: {}", userId, e.getMessage());

            return ResponseEntity.status(400).body(e.getMessage());
        } catch (UserException e) {
            log.warn("User with id {} not found. Error: {}", userId, e.getMessage());

            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            log.warn("Unexpected error during updating username with userId {}. Error: {}",
                    userId, e.getMessage());

            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/update/email")
    public ResponseEntity<?> updateEmail(
            @RequestParam @Valid Long userId,
            @RequestBody @Valid UpdateEmailRequest request
    ) {
        try {
            log.info("Updating email attempt for userId: {}", userId);

            userValidationService.validateEmail(request.email());
            UserResponse response = userService.updateUser(userId, request.email(), UserUpdateType.EMAIL);

            log.info("User with id {} updated email successfully", userId);

            return ResponseEntity.status(200).body(response);
        } catch (ValidationException e) {
            log.warn("Updating email with userId {} validation failed: {}", userId, e.getMessage());

            return ResponseEntity.status(400).body(e.getMessage());
        } catch (UserException e) {
            log.warn("User with id {} not found. Error: {}", userId, e.getMessage());

            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            log.warn("Unexpected error during updating email with userId {}. Error: {}",
                    userId, e.getMessage());

            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}