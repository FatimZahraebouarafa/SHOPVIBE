USE dbVentes;

INSERT INTO articles (description, brand, price, stock, image_url, category)
VALUES
    ('Smartphone Galaxy S24 128 Go', 'Samsung', 799.99, 25, '/images/smartphone.jpg', 'Smartphones'),
    ('Laptop Inspiron 15 pouces i7', 'Dell', 1099.00, 12, '/images/Dell — Laptop 15 pouces i7.jpg', 'Ordinateurs'),
    ('Casque Bluetooth WH-1000XM5', 'Sony', 349.99, 30, '/images/Sony — Casque Bluetooth.jpg', 'Audio'),
    ('Apple Watch Series 9', 'Apple', 429.00, 18, '/images/Apple — Montre connecte.jpg', 'Montres connectées'),
    ('Tablette Tab M10 10 pouces', 'Lenovo', 249.99, 20, '/images/Lenovo — Tablette 10 pouces.jpg', 'Tablettes'),
    ('Clavier mécanique MX', 'Logitech', 89.99, 40, '/images/Logitech — Clavier mécanique.jpg', 'Accessoires'),
    ('Souris sans fil MX Master', 'Logitech', 59.99, 50, '/images/Logitech — Souris sans fil.jpg', 'Accessoires'),
    ('Écran UltraGear 27 pouces 4K', 'LG', 499.00, 10, '/images/LG.jpg', 'Écrans');
