CREATE TABLE chats (
       chat_id BIGINT NOT NULL,
       chat_type VARCHAR(50) NOT NULL,
       title VARCHAR(255),
       avatar_id BIGINT,

       CONSTRAINT pk_chats PRIMARY KEY (chat_id, chat_type)
);