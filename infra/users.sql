INSERT INTO users (username, phone, email, password, created_at, enabled, status)
SELECT
    'user_' || LPAD(seq::text, 7, '0') || '_' || SUBSTRING(MD5(seq::text) FROM 1 FOR 6),
    '+79' || LPAD((1000000 + seq)::text, 7, '0'),
    'user_' || LPAD(seq::text, 7, '0') || '_' || SUBSTRING(MD5(RANDOM()::text) FROM 1 FOR 8) || '@example.com',
    '$2a$10$' || SUBSTRING(MD5(RANDOM()::text) FROM 8 FOR 45),
    NOW() - (RANDOM() * INTERVAL '365 days'),
    (RANDOM() > 0.05),
    CASE
        WHEN RANDOM() < 0.9 THEN 'ACTIVE'
        ELSE 'INACTIVE'
        END
FROM GENERATE_SERIES(1, 3000000) AS seq;