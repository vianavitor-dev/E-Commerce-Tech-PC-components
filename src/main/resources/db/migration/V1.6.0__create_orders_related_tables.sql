CREATE TABLE IF NOT EXISTS orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    status ENUM('in_process', 'on_its_way', 'delivered', 'canceled', 'refunded') NOT NULL DEFAULT 'in_process',
    ordered_at DATE NOT NULL, -- set the default value on the backend
    status_updated_at DATE NOT NULL -- set value on the backend
);

CREATE TABLE IF NOT EXISTS purchased_products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    order_id INT NOT NULL
);

ALTER TABLE orders
ADD CONSTRAINT fk_users_orders
    FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE purchased_products
    ADD CONSTRAINT fk_purchased_products_items
        FOREIGN KEY (product_id) REFERENCES products(id);

ALTER TABLE purchased_products
    ADD CONSTRAINT fk_purchased_products_orders
        FOREIGN KEY (order_id) REFERENCES orders(id);
