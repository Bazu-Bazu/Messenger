package com.burkina.messenger.domain.repository;

import com.burkina.messenger.domain.entity.PersonalChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalChatRepository extends JpaRepository<PersonalChat, Long> {

    @Query("""
        SELECT pc FROM PersonalChat pc
        WHERE (pc.user1Id = :user1Id AND pc.user2Id = :user2Id)
        OR (pc.user1Id = :user2Id AND pc.user2Id = :user1Id)
    """)
    Optional<PersonalChat> findPersonalChatByUsers(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);
}
