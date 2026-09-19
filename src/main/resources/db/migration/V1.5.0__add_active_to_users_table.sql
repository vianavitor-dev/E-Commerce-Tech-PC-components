ALTER TABLE users
ADD COLUMN active BIT NOT NULL DEFAULT 1; -- by default th1e user is active