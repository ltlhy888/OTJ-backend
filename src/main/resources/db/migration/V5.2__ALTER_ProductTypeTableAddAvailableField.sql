ALTER TABLE product_type
ADD available tinyint(1) NOT NULL DEFAULT 1;

UPDATE product_type
SET available = 1;