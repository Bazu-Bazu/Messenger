CREATE TABLE outbox_events(
          id BIGSERIAL PRIMARY KEY,
          topic VARCHAR(255) NOT NULL,
          event_type VARCHAR(255) NOT NULL,
          aggregate_type VARCHAR(255) NOT NULL,
          payload VARCHAR(510) NOT NULL,
          sent BOOLEAN DEFAULT FALSE,
          created_at TIMESTAMP NOT NULL
);