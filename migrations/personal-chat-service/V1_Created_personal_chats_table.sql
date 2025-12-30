CREATE TABLE personal_chats (
                        id BIGSERIAL PRIMARY KEY,
                        user1Id BIGINT NOT NULL,
                        user2Id BIGINT NOT NULL,
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        last_activity_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
