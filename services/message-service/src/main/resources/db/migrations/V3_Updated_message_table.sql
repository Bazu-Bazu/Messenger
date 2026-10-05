ALTER TABLE messages
DROP COLUMN content;

ALTER TABLE messages
ADD COLUMN text VARCHAR(2000);

ALTER TABLE messages
ADD COLUMN media_id BIGINT;

ALTER TABLE messages ADD CONSTRAINT message_payload_check
    CHECK (
        (message_type = 'TEXT' AND text IS NOT NULL AND media_id IS NULL)
            OR (message_type = 'MEDIA' AND media_id IS NOT NULL AND text IS NULL)
        );