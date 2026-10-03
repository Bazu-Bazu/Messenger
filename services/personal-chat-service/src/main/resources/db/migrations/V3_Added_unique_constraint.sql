ALTER TABLE personal_chats
ADD CONSTRAINT uk_users UNIQUE (user1Id, user2Id);
