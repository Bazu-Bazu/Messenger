CREATE TABLE refresh_tokens (
                                id BIGSERIAL PRIMARY KEY,
                                token VARCHAR(255) UNIQUE NOT NULL,
                                user_id BIGINT NOT NULL,
                                expires_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
                                created_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
                                device_info VARCHAR(255) NOT NULL,
                                ip_address VARCHAR(255) NOT NULL,
                                revoked_at TIMESTAMP(6) WITH TIME ZONE
);