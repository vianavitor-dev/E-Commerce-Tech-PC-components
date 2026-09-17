CREATE TABLE rating_history(
    id INT PRIMARY KEY auto_increment,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    rate DECIMAL(2, 1) NOT NULL DEFAULT 0.0
);

ALTER TABLE rating_history
ADD CONSTRAINT fk_rating_history_user
    FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE rating_history
ADD CONSTRAINT fk_rating_history_product
    FOREIGN KEY (product_id) REFERENCES products(id);