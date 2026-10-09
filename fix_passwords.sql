USE dbVentes;

-- Fix password hashes for all users
UPDATE users SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFWwFMqkJrR1y7Vn7yDQXa2' WHERE login = 'alice';
UPDATE users SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFWwFMqkJrR1y7Vn7yDQXa2' WHERE login = 'bob';
UPDATE users SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFWwFMqkJrR1y7Vn7yDQXa2' WHERE login = 'admin';
UPDATE users SET password = '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFWwFMqkJrR1y7Vn7yDQXa2', role = 'ROLE_USER' WHERE login = 'test';

-- Verify the updates
SELECT login, password, role FROM users;
