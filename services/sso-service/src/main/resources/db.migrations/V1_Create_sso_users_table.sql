CREATE TABLE sso_users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(25) UNIQUE NOT NULL,
                       phone VARCHAR(15) UNIQUE NOT NULL,
                       email VARCHAR(255) UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       enabled BOOLEAN DEFAULT true
);