CREATE TABLE group_chats(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    avatar_url VARCHAR(255),
    created_by BIGINT NOT NULL,
    description VARCHAR(200),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_activity_at TIMESTAMP WITH TIME ZONE NOT NULL
);