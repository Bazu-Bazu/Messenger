package messenger.group.chat.service.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import messenger.group.chat.service.domain.enums.GroupMemberRole;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_chat_members")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private GroupChat group;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private GroupMemberRole role = GroupMemberRole.MEMBER;

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime joinedAt = LocalDateTime.now();

    public boolean canSendMessage() {
        return role.canSendMessage();
    }

    public boolean canInviteMembers() {
        return role.canInviteMembers();
    }

    public boolean canRemoveMembers() {
        return role.canRemoveMembers();
    }

    public boolean canChangeGroupInfo() {
        return role.canChangeGroupInfo();
    }

    public boolean canSetRole(GroupMemberRole role) {
        return this.role.canSetRole(role);
    }

    public boolean canDeleteGroup() {
        return role.canDeleteGroup();
    }

    public boolean canManage(GroupMemberRole role) {
        return this.role.canManage(role);
    }
}
