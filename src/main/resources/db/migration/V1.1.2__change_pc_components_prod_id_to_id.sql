ALTER TABLE cpus
RENAME COLUMN product_id TO id;

ALTER TABLE gpus
RENAME COLUMN product_id TO id;

ALTER TABLE power_supply_units
RENAME COLUMN product_id TO id;

ALTER TABLE rams
RENAME COLUMN product_id TO id;

ALTER TABLE ssds
RENAME COLUMN product_id TO id;

ALTER TABLE motherboards
RENAME COLUMN product_id TO id;
