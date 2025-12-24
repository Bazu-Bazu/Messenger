package messenger.user.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import messenger.user.service.dto.request.UpdateProfileRequest;
import messenger.user.service.dto.response.ProfileResponse;
import messenger.user.service.exception.UserException;
import messenger.user.service.exception.ValidationException;
import messenger.user.service.service.ProfileService;
import messenger.user.service.service.ProfileValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@Log4j2
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileValidationService profileValidationService;

    @PatchMapping("/update")
    public ResponseEntity<?> updateProfile(
            @RequestParam @Valid Long userId,
            @RequestBody @Valid UpdateProfileRequest request
    ) {
        try {
            log.info("Updating profile attempt for userId: {}", userId);

            profileValidationService.validateProfileUpdating(request.firstName(), request.lastName(), request.bio());
            ProfileResponse response = profileService.updateProfile(userId, request);

            log.info("User with id {} updated profile successfully", userId);

            return ResponseEntity.status(200).body(response);
        } catch (ValidationException e) {
            log.warn("Updating profile with userId {} validation failed: {}", userId, e.getMessage());

            return ResponseEntity.status(400).body(e.getMessage());
        } catch (UserException e) {
            log.warn("User with id {} not found. Error: {}", userId, e.getMessage());

            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            log.warn("Unexpected error during updating profile with userId {}. Error: {}",
                    userId, e.getMessage());

            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/avatar_url")
    public ResponseEntity<?> addAvatarUrl(
            @RequestParam @Valid Long userId,
            @RequestParam @Valid String url
    ) {
        try {
            log.info("Adding avatar url attempt for userId: {}", userId);

            ProfileResponse response = profileService.addAvatarUrl(userId, url);

            log.info("User with id {} added avatar url successfully", userId);

            return ResponseEntity.status(200).body(response);
        } catch (UserException e) {
            log.warn("User with id {} not found. Error: {}", userId, e.getMessage());

            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            log.warn("Unexpected error during adding avatar url with userId {}. Error: {}",
                    userId, e.getMessage());

            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable @Valid Long userId) {
        try {
            log.info("Getting profile attempt for userId: {}", userId);

            ProfileResponse response = profileService.getProfile(userId);

            log.info("Got profile with user id {} successfully", userId);

            return ResponseEntity.status(200).body(response);
        } catch (UserException e) {
            log.warn("User with id {} not found. Error: {}", userId, e.getMessage());

            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            log.warn("Unexpected error during getting profile with userId {}. Error: {}",
                    userId, e.getMessage());

            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}