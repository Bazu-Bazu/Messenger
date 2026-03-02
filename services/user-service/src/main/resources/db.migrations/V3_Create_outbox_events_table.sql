CREATE TABLE outbox_events(
    id BIGSERIAL PRIMARY KEY,
    topic VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    sent BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL
);

ALTER TABLE outbox_events ADD CONSTRAINT event_type_check
    CHECK (event_type IN (
                          'USER_REGISTERED',
                          'USER_EMAIL_UPDATED',
                          'USER_PHONE_UPDATED',
                          'USER_PASSWORD_UPDATED',
                          'USER_USERNAME_UPDATED'
                         ));