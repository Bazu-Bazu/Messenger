ALTER TABLE group_chats
DROP COLUMN avatar_url;

ALTER TABLE group_chats
    ADD COLUMN avatar_id BIGINT;