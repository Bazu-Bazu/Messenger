package messenger.group.chat.service.dto.response;

import lombok.*;
import messenger.group.chat.service.domain.enums.GroupMemberRole;
import messenger.group.chat.service.dto.UserInfoDto;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberResponse {

    private Long id;
    private GroupMemberRole role;
    private UserInfoDto userInfo;
}
