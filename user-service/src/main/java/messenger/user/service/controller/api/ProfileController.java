package messenger.user.service.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import messenger.user.service.dto.request.UpdateProfileRequest;
import messenger.user.service.dto.response.ProfileResponse;
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
            @RequestParam("userId") @Valid Long userId,
            @RequestBody @Valid UpdateProfileRequest request
    ) {
        log.info("Updating profile attempt for userId: {}", userId);

        profileValidationService.validateProfileUpdating(request.firstName(), request.lastName(), request.bio());
        ProfileResponse response = profileService.updateProfile(userId, request);

        log.info("User with id {} updated profile successfully", userId);

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/avatar_url")
    public ResponseEntity<?> addAvatarUrl(
            @RequestParam("userId") @Valid Long userId,
            @RequestParam("url") @Valid String url
    ) {
        log.info("Adding avatar url attempt for userId: {}", userId);

        ProfileResponse response = profileService.addAvatarUrl(userId, url);

        log.info("User with id {} added avatar url successfully", userId);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable @Valid Long userId) {
        log.info("Getting profile attempt for userId: {}", userId);

        ProfileResponse response = profileService.getProfile(userId);

        log.info("Got profile with user id {} successfully", userId);

        return ResponseEntity.status(200).body(response);
    }

}