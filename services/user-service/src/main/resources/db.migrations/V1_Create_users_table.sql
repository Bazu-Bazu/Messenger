CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(25) UNIQUE NOT NULL,
                       phone VARCHAR(15) UNIQUE NOT NULL,
                       email VARCHAR(255) UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       enabled BOOLEAN DEFAULT true,
                       status VARCHAR(255) DEFAULT 'ACTIVE',
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE,

                       first_name VARCHAR(25),
                       last_name VARCHAR(25),
                       bio VARCHAR(500),
                       avatar_url VARCHAR(255),
                       birth_date DATE,
                       gender VARCHAR(255)
);

ALTER TABLE users ADD CONSTRAINT users_gender_check
    CHECK (gender IN ('MALE', 'FEMALE'));

ALTER TABLE users ADD CONSTRAINT users_status_check
    CHECK (status IN ('ACTIVE', 'INACTIVE', 'DELETED'));