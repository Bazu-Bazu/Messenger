CREATE TABLE messages (
                        id BIGSERIAL PRIMARY KEY,
                        chat_id BIGINT NOT NULL,
                        chat_type VARCHAR(10) NOT NULL,
                        content VARCHAR(2000) NOT NULL,
                        sender_id BIGINT NOT NULL,
                        message_type VARCHAR(5) NOT NULL,
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        edited_at TIMESTAMP WITH TIME ZONE,
                        read_at TIMESTAMP WITH TIME ZONE
);

ALTER TABLE messages ADD CONSTRAINT message_type_check
    CHECK (message_type IN ('TEXT', 'IMAGE', 'FILE'));

ALTER TABLE messages ADD CONSTRAINT chat_type_check
    CHECK (chat_type IN ('PERSONAL', 'GROUP'));