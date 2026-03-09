package messenger.group.chat.service.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import messenger.group.chat.service.dto.request.ChangeGroupInfoRequest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "group_chats")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String name;

    private String avatarUrl;

    @Column(nullable = false)
    private Long createdBy;

    @Column(length = 200)
    private String description;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<GroupChatMember> members = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    @Builder.Default
    private Instant lastActivityAt = Instant.now();

    public void addMember(GroupChatMember member) {
        members.add(member);
        member.setGroup(this);
    }

    public void addMembers(List<GroupChatMember> members) {
        members.forEach(this::addMember);
    }

    public void changeFrom(ChangeGroupInfoRequest request) {
        if (request.name() != null) {
            this.name = request.name();
        }
        if (request.description() != null) {
            this.description = request.description();
        }
        if (request.avatarUrl() != null) {
            this.avatarUrl = request.avatarUrl();
        }
    }
}
