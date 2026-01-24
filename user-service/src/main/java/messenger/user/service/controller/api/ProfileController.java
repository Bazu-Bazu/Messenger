package messenger.user.service.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @PatchMapping("/update")
    public ResponseEntity<?> updateProfile(
            @RequestParam("userId") @Valid Long userId,
            @RequestBody @Valid UpdateProfileRequest request
    ) {
        profileValidationService.validateProfileUpdating(request.firstName(), request.lastName(), request.bio());
        ProfileResponse response = profileService.updateProfile(userId, request);

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/avatar_url")
    public ResponseEntity<?> addAvatarUrl(
            @RequestParam("userId") @Valid Long userId,
            @RequestParam("url") @Valid String url
    ) {
        ProfileResponse response = profileService.addAvatarUrl(userId, url);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getProfile(@RequestParam("userId") @Valid Long userId) {
        ProfileResponse response = profileService.getProfile(userId);

        return ResponseEntity.status(200).body(response);
    }

}