package messenger.group.chat.service.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import messenger.group.chat.service.domain.enums.GroupMemberRole;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "group_chat_members")
@Data
@Builder
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
    private GroupMemberRole role;

    @CreationTimestamp
    private LocalDateTime joinedAt;

    @Column(length = 25)
    private String customNickname;

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

    public boolean canSetRole() {
        return role.canSetRole();
    }

    public boolean canManage(GroupMemberRole role) {
        return this.role.canManage(role);
    }

}
