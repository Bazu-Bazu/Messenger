package messenger.user.service.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import messenger.user.service.dto.request.AddAvatarRequest;
import messenger.user.service.dto.request.UpdateProfileRequest;
import messenger.user.service.dto.response.ProfileResponse;
import messenger.user.service.service.ProfileService;
import messenger.user.service.service.ProfileValidationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileValidationService profileValidationService;

    @PatchMapping
    public ResponseEntity<?> updateProfile(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid UpdateProfileRequest request
    ) {
        profileValidationService.validateProfileUpdating(request.firstName(), request.lastName(), request.bio());
        ProfileResponse response = profileService.updateProfile(userId, request);

        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/avatar")
    public ResponseEntity<?> addAvatarUrl(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid AddAvatarRequest request
    ) {
        ProfileResponse response = profileService.addAvatarUrl(userId, request);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getProfile(@PathVariable("userId") @Valid Long userId) {
        ProfileResponse response = profileService.getProfile(userId);

        return ResponseEntity.status(200).body(response);
    }

}