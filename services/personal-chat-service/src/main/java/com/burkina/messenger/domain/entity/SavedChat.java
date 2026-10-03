package com.burkina.messenger.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "saved_chats")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SavedChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

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
