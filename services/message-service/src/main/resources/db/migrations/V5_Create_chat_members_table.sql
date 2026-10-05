CREATE TABLE chat_members (
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    chat_type VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    can_send_message BOOLEAN NOT NULL,
    last_read_message_id BIGINT,
    joined_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_chat_members_chat_type_user UNIQUE (chat_id, chat_type, user_id)
);