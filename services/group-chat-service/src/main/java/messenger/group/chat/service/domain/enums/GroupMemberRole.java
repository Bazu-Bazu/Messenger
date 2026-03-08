package messenger.group.chat.service.domain.enums;

public enum GroupMemberRole {

    OWNER(100),
    ADMIN(75),
    MEMBER(50),
    BANNED(0);

    private final int priority;

    GroupMemberRole(int priority) {
        this.priority = priority;
    }

    public boolean canInviteMembers() {
        return this.priority >= ADMIN.priority;
    }

    public boolean canRemoveMembers() {
        return this.priority >= ADMIN.priority;
    }

    public boolean canChangeGroupInfo() {
        return this.priority >= ADMIN.priority;
    }

    public boolean canSetRole(GroupMemberRole other) {
        return this.priority >= ADMIN.priority && this.priority > other.priority;
    }

    public boolean canSendMessage() {
        return this.priority >= MEMBER.priority;
    }

    public boolean canDeleteGroup() {
        return this.priority >= OWNER.priority;
    }

    public boolean canManage(GroupMemberRole other) {
        return this.priority > other.priority;
    }
}
