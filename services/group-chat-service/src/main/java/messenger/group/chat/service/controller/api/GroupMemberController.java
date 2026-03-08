package messenger.group.chat.service.controller.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.dto.request.AddNewMembersRequest;
import messenger.group.chat.service.dto.request.RemoveMembersRequest;
import messenger.group.chat.service.dto.request.SetRolesRequest;
import messenger.group.chat.service.dto.response.GroupMemberResponse;
import messenger.group.chat.service.service.GroupService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chats/group/member")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class GroupMemberController {

    private final GroupService groupChatService;

    @PostMapping("/{groupId}")
    public ResponseEntity<List<GroupMemberResponse>> addNewMembers(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long invitorId,
            @PathVariable("groupId") Long groupId,
            @RequestBody @Valid AddNewMembersRequest request
    ) {
        List<GroupMemberResponse> responses = groupChatService.addNewMembers(invitorId, groupId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> removeMembers(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long removerId,
            @PathVariable("groupId") Long groupId,
            @RequestBody @Valid RemoveMembersRequest request
    ) {
        groupChatService.removeMembers(removerId, groupId, request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<List<GroupMemberResponse>> getMembers(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable("groupId") Long groupId,
            Pageable pageable
    ) {
        List<GroupMemberResponse> responses = groupChatService.getGroupMembers(userId, groupId, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PatchMapping("/{groupId}")
    public ResponseEntity<?> setRoles(
            @Parameter(hidden = true)
            @RequestHeader("X-User-Id") Long setterId,
            @PathVariable("groupId") Long groupId,
            @RequestBody @Valid SetRolesRequest request)
    {
        List<GroupMemberResponse> responses = groupChatService.setRoles(setterId, groupId, request);

        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
