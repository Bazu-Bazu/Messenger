CREATE TABLE chat_members (
                             id BIGSERIAL PRIMARY KEY,
                             chat_id BIGINT NOT NULL,
                             user_id BIGINT NOT NULL,
                             role VARCHAR(50) NOT NULL,
                             joined_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                             user_username VARCHAR(255),

                             CONSTRAINT fk_chat_member_chat
                                 FOREIGN KEY (chat_id)
                                     REFERENCES chats(id)
                                     ON DELETE CASCADE
);

ALTER TABLE chat_members ADD CONSTRAINT chat_member_role_check
    CHECK (role IN ('OWNER', 'MEMBER', 'READONLY'));