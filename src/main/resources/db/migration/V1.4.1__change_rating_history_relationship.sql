ALTER TABLE rating_history
DROP PRIMARY KEY,
DROP COLUMN id;

ALTER TABLE rating_history
ADD CONSTRAINT pk_rating_history
    PRIMARY KEY (user_id, product_id);