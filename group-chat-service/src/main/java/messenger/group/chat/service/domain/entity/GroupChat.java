package messenger.group.chat.service.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import messenger.group.chat.service.dto.request.ChangeGroupInfoRequest;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "group_chats")
@Data
@Builder
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

    @CreationTimestamp
    @Column(nullable = false)
    private Instant createdAt;

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
