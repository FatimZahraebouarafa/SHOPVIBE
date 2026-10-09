-- ─── Utilisateurs de test ────────────────────────────────────────
-- Le mot de passe est encodé en BCrypt
INSERT INTO users (login, password, connection_number, role) VALUES
    ('alice',  '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFWwFMqkJrR1y7Vn7yDQXa2', 0, 'ROLE_USER'),
    ('bob',    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFWwFMqkJrR1y7Vn7yDQXa2', 0, 'ROLE_USER')
ON DUPLICATE KEY UPDATE
    password = VALUES(password),
    connection_number = VALUES(connection_number),
    role = VALUES(role);

-- ─── Articles de test ────────────────────────────────────────────
INSERT IGNORE INTO articles (description, brand, price, stock) VALUES
    ('Smartphone 128Go',        'Samsung',  599.99, 50),
    ('Laptop 15 pouces i7',     'Dell',    1099.00, 20),
    ('Casque Bluetooth',        'Sony',     149.99, 100),
    ('Montre connectée',        'Apple',    399.00, 30),
    ('Tablette 10 pouces',      'Lenovo',   349.99, 40),
    ('Clavier mécanique',       'Logitech',  89.99, 75),
    ('Souris sans fil',         'Logitech',  39.99, 120),
    ('Écran 27 pouces 4K',      'LG',       499.00, 15);
