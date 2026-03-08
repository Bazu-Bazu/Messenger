package messenger.group.chat.service.validator;

import lombok.RequiredArgsConstructor;
import messenger.group.chat.service.domain.repository.GroupMemberRepository;
import messenger.group.chat.service.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GroupValidator {

    private final GroupMemberRepository groupChatMemberRepository;

    public void validateCreatorNotInMembers(Long creatorId, List<Long> members) {
        Set<Long> membersSet = new HashSet<>(members);

        if (membersSet.contains(creatorId)) {
            throw new BadRequestException("Creator cannot be a member");
        }
    }

    public void validateMembersNotAlreadyInGroup(Long groupId, List<Long> members) {
        Set<Long> existing = groupChatMemberRepository.findAllUserIdsByGroupId(groupId);

        for (Long member : members) {
            if (existing.contains(member)) {
                throw new BadRequestException(
                        String.format("User %d already in group %d", member, groupId)
                );
            }
        }
    }
}
