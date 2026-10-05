CREATE TABLE media_metadata(
    id BIGSERIAL PRIMARY KEY,
    media_name VARCHAR(255) NOT NULL ,
    url VARCHAR(255) NOT NULL UNIQUE,
    size BIGINT NOT NULL,
    type VARCHAR(255) NOT NULL
);

ALTER TABLE media_metadata ADD CONSTRAINT media_type_check
    CHECK (type IN ('FILE', 'IMAGE', 'VIDEO'));