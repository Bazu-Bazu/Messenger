ALTER TABLE outbox_events DROP CONSTRAINT event_type_check;

ALTER TABLE outbox_events ADD CONSTRAINT event_type_check
    CHECK (event_type IN (
                          'USER_REGISTERED',
                          'USER_EMAIL_UPDATED',
                          'USER_PHONE_UPDATED',
                          'USER_PASSWORD_UPDATED',
                          'USER_USERNAME_UPDATED',
                          'USER_AVATAR_UPDATED'
        ));