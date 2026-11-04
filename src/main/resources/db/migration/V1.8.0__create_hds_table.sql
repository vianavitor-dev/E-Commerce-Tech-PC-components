CREATE TABLE IF NOT EXISTS hds(
    id INT NOT NULL,
    model VARCHAR(50) NOT NULL,
    capacity_gb SMALLINT NOT NULL,
    read_speed_mb SMALLINT NOT NULL,
    write_speed_mb SMALLINT NOT NULL
);

ALTER TABLE hds
ADD CONSTRAINT fk_product_hd
FOREIGN KEY (id) REFERENCES products(id)
ON DELETE CASCADE;