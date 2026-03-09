package messenger.group.chat.service.dto.response;

import lombok.*;

import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponse {

    private Long id;
    private String name;
    private String avatarUrl;
    private String description;
    private Long createdBy;
    private Instant createdAt;
    private Instant lastActivityAt;
}
