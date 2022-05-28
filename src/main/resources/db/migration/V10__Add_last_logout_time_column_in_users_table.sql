ALTER TABLE users
    ADD COLUMN last_logout_time DATETIME;

UPDATE users SET last_logout_time = now();