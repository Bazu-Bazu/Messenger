package messenger.user.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import messenger.user.service.dto.request.CreateUserRequest;
import messenger.user.service.dto.response.UserResponse;
import messenger.user.service.exception.ValidationException;
import messenger.user.service.service.UserService;
import messenger.user.service.service.UserValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

            log.info("User with phone {} registered successfully", response.getPhone());

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

}
