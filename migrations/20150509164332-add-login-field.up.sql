DELETE FROM users;
ALTER TABLE users ADD COLUMN login text UNIQUE NOT NULL;