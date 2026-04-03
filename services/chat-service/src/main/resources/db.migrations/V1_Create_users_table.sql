CREATE TABLE users(
    user_id BIGINT PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    avatar_id BIGINT
)