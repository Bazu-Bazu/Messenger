CREATE TABLE group_chat_members(
    id BIGSERIAL PRIMARY KEY,
    group_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role VARCHAR(6) NOT NULL,
    joined_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    custom_nickname VARCHAR(25),

    CONSTRAINT fk_group_chat_member_group
        FOREIGN KEY (group_id)
            REFERENCES group_chats(id)
            ON DELETE CASCADE
);

ALTER TABLE group_chat_members ADD CONSTRAINT group_chat_member_role_check
    CHECK (role IN ('OWNER', 'MEMBER', 'ADMIN', 'BANNED'));