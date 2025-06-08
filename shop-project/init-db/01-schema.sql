DROP TABLE IF EXISTS order_items, orders, cart_items, carts, products, users;


CREATE TABLE USERS (
    id SERIAL PRIMARY KEY ,
    name VARCHAR(50) NOT NULL,
    login VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL
);


CREATE TABLE PRODUCTS (
    id SERIAL PRIMARY KEY ,
    name VARCHAR(255) UNIQUE NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    description TEXT
);


CREATE TABLE ORDERS (
    id SERIAL PRIMARY KEY ,
    user_id INTEGER NOT NULL,
    delivery_address VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES USERS(id)
);


CREATE TABLE CARTS (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL UNIQUE,
    FOREIGN KEY (user_id) REFERENCES USERS(id)
);

CREATE TABLE CART_ITEMS (
    id SERIAL PRIMARY KEY ,
    cart_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    FOREIGN KEY (cart_id) REFERENCES CARTS(id),
    FOREIGN KEY (product_id) REFERENCES PRODUCTS(id)
);


CREATE TABLE ORDER_ITEMS (
    id SERIAL PRIMARY KEY ,
    order_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES ORDERS(id),
    FOREIGN KEY (product_id) REFERENCES PRODUCTS(id)
);


CREATE TABLE ORDER_STATUS_HISTORY (
    id SERIAL PRIMARY KEY ,
    order_id INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES ORDERS(id)
);


CREATE INDEX idx_orders_user_id ON ORDERS(user_id);
CREATE INDEX idx_orders_status ON ORDERS(status);
CREATE INDEX idx_carts_user_id ON CARTS(user_id);
CREATE INDEX idx_cart_items_cart_id ON CART_ITEMS(cart_id);
CREATE INDEX idx_cart_items_product_id ON CART_ITEMS(product_id);
CREATE INDEX idx_order_items_order_id ON ORDER_ITEMS(order_id);
CREATE INDEX idx_order_items_product_id ON ORDER_ITEMS(product_id);
CREATE INDEX idx_order_status_history_order_id ON ORDER_STATUS_HISTORY(order_id);