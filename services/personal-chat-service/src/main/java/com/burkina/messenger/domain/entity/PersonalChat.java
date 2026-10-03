package com.burkina.messenger.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
        name = "personal_chats",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user1Id", "user2Id"})
        }
)
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    Long user1Id;

    @Column(nullable = false)
    Long user2Id;

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    @Builder.Default
    private boolean deleted = false;

    public boolean cancelDeletion() {
        if (this.deleted) {
            this.deleted = false;

            return true;
        }

        return false;
    }

    public boolean delete() {
        if (!this.deleted) {
            this.deleted = true;

            return true;
        }

        return false;
    }
}
