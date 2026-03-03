package messenger.user.service.controller.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import messenger.user.service.dto.request.AddAvatarRequest;
import messenger.user.service.dto.request.UpdateProfileRequest;
import messenger.user.service.dto.response.ProfileResponse;
import messenger.user.service.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PatchMapping
    public ResponseEntity<ProfileResponse> updateProfile(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid UpdateProfileRequest request
    ) {
        ProfileResponse response = profileService.updateProfile(userId, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/avatar")
    public ResponseEntity<ProfileResponse> addAvatarUrl(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody @Valid AddAvatarRequest request
    ) {
        ProfileResponse response = profileService.addAvatarUrl(userId, request);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable("userId") Long userId) {
        ProfileResponse response = profileService.getProfile(userId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<ProfileResponse> getMyProfile(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId
    ) {
        ProfileResponse response = profileService.getProfile(userId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}