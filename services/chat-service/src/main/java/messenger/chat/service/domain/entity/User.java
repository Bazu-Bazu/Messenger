package messenger.chat.service.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private Long userId;

    @Column(unique = true)
    private String username;

    private Long avatarId;
}
