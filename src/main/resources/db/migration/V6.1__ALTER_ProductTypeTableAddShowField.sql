ALTER TABLE product_type
ADD display tinyint(1) NOT NULL DEFAULT 1;

UPDATE product_type
SET display = 1;