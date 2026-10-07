CREATE TABLE user_chats (
        id BIGSERIAL PRIMARY KEY,
        chat_id BIGINT NOT NULL,
        chat_type VARCHAR(50) NOT NULL,
        user_id BIGINT NOT NULL,
        can_send BOOLEAN NOT NULL,

        CONSTRAINT fk_user_chats_chat
            FOREIGN KEY (chat_id, chat_type)
                REFERENCES chats (chat_id, chat_type)
                ON DELETE CASCADE,

        CONSTRAINT fk_user_chats_user
            FOREIGN KEY (user_id)
                REFERENCES users (user_id)
                ON DELETE CASCADE,

        CONSTRAINT uk_user_chat UNIQUE (user_id, chat_id, chat_type)
);