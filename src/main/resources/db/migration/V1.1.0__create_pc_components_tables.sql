CREATE TABLE products (
    id INT PRIMARY KEY auto_increment,
    sku VARCHAR(100) NOT NULL UNIQUE,
    rating DECIMAL(2, 1) NOT NULL DEFAULT 0.0,
    rated_count INT NOT NULL DEFAULT 0,
    brand VARCHAR(30) NOT NULL,
    category ENUM('CPU', 'GPU', 'RAM', 'Motherboard', 'SSD', "PSU") NOT NULL,
    short_description VARCHAR(255) NOT NULL,
    technical_description TEXT NOT NULL,
    price DECIMAL(8, 2) NOT NULL,
    stock SMALLINT NOT NULL DEFAULT 1
);

CREATE TABLE motherboards (
  product_id INT PRIMARY KEY,
  model VARCHAR(50) NOT NULL,
  chipset VARCHAR(20) NOT NULL,
  socket VARCHAR(10) NOT NULL,
  ddr_generation VARCHAR(4) NOT NULL,
  ram_slots TINYINT NOT NULL,
  max_ram_capacity_gb INT NOT NULL,
  -- the ports only show how many there are so far
  -- TODO: make it more detailed, creating a 'ports' table for example
  sata_slots TINYINT NOT NULL,
  m2_slots TINYINT NOT NULL DEFAULT 0,
  form_factor ENUM('Mini-ITX', 'mATX', 'ATX', 'E-ATX') NOT NULL DEFAULT 'ATX'
);

CREATE TABLE cpus (
  product_id INT PRIMARY KEY,
  model VARCHAR(50) NOT NULL,
  manufacturer VARCHAR(20) NOT NULL,
  socket VARCHAR(10) NOT NULL,
  threads TINYINT NOT NULL,
  cores TINYINT NOT NULL,
  base_clock DECIMAL(3, 1) NOT NULL,
  boost_clock DECIMAL(3, 1) NOT NULL,
  tdp_watts TINYINT NOT NULL
);

CREATE TABLE gpus (
  product_id INT PRIMARY KEY,
  model VARCHAR(30) NOT NULL,
  chipset VARCHAR(20) NOT NULL,
  vram_gb TINYINT NOT NULL,
  vram_type VARCHAR(10) NOT NULL,
  power_consumption SMALLINT NOT NULL,
  interface VARCHAR(20) NOT NULL,
  recommended_psu_watts SMALLINT
);

CREATE TABLE rams (
  product_id INT PRIMARY KEY,
  model VARCHAR(50) NOT NULL,
  capacity_gb TINYINT NOT NULL,
  ddr_generation VARCHAR(4) NOT NULL,
  frequency_mhz SMALLINT NOT NULL,
  modules TINYINT NOT NULL,
  form_factor ENUM('DIMM', 'SO-DIMM') NOT NULL DEFAULT 'DIMM'
);

CREATE TABLE ssds (
  product_id INT PRIMARY KEY,
  model VARCHAR(50) NOT NULL,
  capacity_gb SMALLINT NOT NULL,
  interface ENUM('SATA', 'PCIe') NOT NULL DEFAULT 'SATA',
  form_factor ENUM('M.2', '2.5"', 'mSATA', 'U.2') NOT NULL DEFAULT '2.5"',
  protocol ENUM('NVMe', 'AHCI') NOT NULL DEFAULT 'AHCI',
  read_speed_mb SMALLINT NOT NULL,
  write_speed_mb SMALLINT NOT NULL
);

CREATE TABLE power_supply_units (
  product_id INT PRIMARY KEY,
  model VARCHAR(50) NOT NULL,
  wattage_supply SMALLINT NOT NULL,
  efficiency_rating ENUM(
    'Standard',
    'Bronze',
    'Silver',
    'Gold',
    'Platinum',
    'Titanium'
  ) NOT NULL DEFAULT 'Standard',
  modularity ENUM('non-modular', 'semi-modular', 'fully-modular') NOT NULL,
  form_factor ENUM('ATX', 'SFX', 'SFX-L', 'TFX') NOT NULL DEFAULT 'ATX'
);

-- SET FOREIGN KEY TO ALL PC COMPONENTS 'product_id' (1:1 Relationship)
ALTER TABLE power_supply_units
ADD CONSTRAINT fk_product_psu
FOREIGN KEY (product_id) REFERENCES products (id)
ON DELETE CASCADE;

ALTER TABLE motherboards
ADD CONSTRAINT fk_product_motherboard
FOREIGN KEY (product_id) REFERENCES products (id)
ON DELETE CASCADE;

ALTER TABLE ssds
ADD CONSTRAINT fk_product_ssd
FOREIGN KEY (product_id) REFERENCES products (id)
ON DELETE CASCADE;

ALTER TABLE rams
ADD CONSTRAINT fk_product_ram
FOREIGN KEY (product_id) REFERENCES products (id)
ON DELETE CASCADE;

ALTER TABLE gpus
ADD CONSTRAINT fk_product_gpu
FOREIGN KEY (product_id) REFERENCES products (id)
ON DELETE CASCADE;

ALTER TABLE cpus
ADD CONSTRAINT fk_product_cpu
FOREIGN KEY (product_id) REFERENCES products (id)
ON DELETE CASCADE;