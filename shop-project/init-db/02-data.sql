
INSERT INTO USERS (name, login, password_hash) VALUES 
('Иван Иванов', 'ivan', 'hash_password_1'),
('Петр Петров', 'petr', 'hash_password_2'),
('Мария Сидорова', 'maria', 'hash_password_3');


INSERT INTO PRODUCTS (name, price, description) VALUES 
('Ноутбук Dell XPS 13', 89999.99, 'Ультрабук с процессором Intel Core i7'),
('Смартфон iPhone 15', 79999.00, 'Флагманский смартфон Apple'),
('Наушники Sony WH-1000XM5', 24999.00, 'Беспроводные наушники с шумоподавлением'),
('Клавиатура Logitech MX Keys', 7999.00, 'Беспроводная клавиатура для профессионалов'),
('Монитор LG 27" 4K', 34999.00, 'IPS монитор с разрешением 4K');


INSERT INTO CARTS (user_id) VALUES (1), (2), (3);


INSERT INTO CART_ITEMS (cart_id, product_id, quantity) VALUES 
(1, 1, 1),
(1, 4, 2),
(2, 2, 1);


INSERT INTO ORDERS (user_id, delivery_address, status) VALUES 
(1, 'г. Москва, ул. Ленина, д. 1, кв. 10', 'processing'),
(2, 'г. Санкт-Петербург, пр. Невский, д. 50, кв. 25', 'delivered');


INSERT INTO ORDER_ITEMS (order_id, product_id, quantity, price) VALUES 
(1, 3, 1, 24999.00),
(1, 5, 1, 34999.00),
(2, 2, 1, 79999.00);


INSERT INTO ORDER_STATUS_HISTORY (order_id, status) VALUES 
(1, 'new'),
(1, 'processing'),
(2, 'new'),
(2, 'processing'),
(2, 'shipped'),
(2, 'delivered');