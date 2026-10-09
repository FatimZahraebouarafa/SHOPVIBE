USE dbVentes;

-- Update passwords to plain text for testing with NoOpPasswordEncoder
UPDATE users SET password = 'password123' WHERE login = 'alice';
UPDATE users SET password = 'password123' WHERE login = 'bob';
UPDATE users SET password = 'password123' WHERE login = 'admin';
UPDATE users SET password = 'password123' WHERE login = 'test';

-- Verify the updates
SELECT login, password, role FROM users;
