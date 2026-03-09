package messenger.group.chat.service.validator;

import messenger.group.chat.service.domain.repository.GroupMemberRepository;
import messenger.group.chat.service.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupValidatorTest {

    @Mock
    private GroupMemberRepository groupChatMemberRepository;

    @InjectMocks
    private GroupValidator groupValidator;

    @Test
    void shouldPassWhenCreatorNotInMembers() {
        Long creatorId = 1L;
        List<Long> members = List.of(2L, 3L);

        assertDoesNotThrow(() ->
                groupValidator.validateCreatorNotInMembers(creatorId, members)
        );
    }

    @Test
    void shouldThrowExceptionWhenCreatorInMembers() {
        Long creatorId = 1L;
        List<Long> members = List.of(1L, 2L);

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> groupValidator.validateCreatorNotInMembers(creatorId, members)
        );

        assertEquals("Creator cannot be a member", exception.getMessage());
    }

    @Test
    void shouldPassWhenMembersNotInGroup() {
        Long groupId = 10L;
        List<Long> members = List.of(2L, 3L);

        when(groupChatMemberRepository.findAllUserIdsByGroupId(groupId))
                .thenReturn(Set.of(5L, 6L));

        assertDoesNotThrow(() ->
                groupValidator.validateMembersNotAlreadyInGroup(groupId, members)
        );
    }

    @Test
    void shouldThrowExceptionWhenMemberAlreadyInGroup() {
        Long groupId = 10L;
        List<Long> members = List.of(2L, 3L);

        when(groupChatMemberRepository.findAllUserIdsByGroupId(groupId))
                .thenReturn(Set.of(3L, 5L));

        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> groupValidator.validateMembersNotAlreadyInGroup(groupId, members)
        );

        assertEquals("User 3 already in group 10", exception.getMessage());
    }
}