CREATE TABLE chats (
                      id BIGSERIAL PRIMARY KEY,
                      type VARCHAR(7) NOT NULL,
                      name VARCHAR(40),
                      description VARCHAR(500),
                      avatar_url VARCHAR(255),
                      created_by BIGINT,
                      created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE chats ADD CONSTRAINT chat_type_check
    CHECK (type IN ('PRIVATE', 'GROUP', 'CHANNEL'));

-- Создаем функцию для обновления updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
    RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Создаем триггер для таблицы users
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON chats
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();