package messenger.personal.chat.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalChatResponse {

    private Long id;
    private Long user1Id;
    private Long user2Id;
    private Instant createdAt;
    private Instant lastActivityAt;
}
