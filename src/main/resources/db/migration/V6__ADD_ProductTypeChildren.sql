CREATE TABLE `product_type_children` (
  `child_id` bigint(20) NOT NULL,
  `product_type_id` bigint(20) NOT NULL,
  PRIMARY KEY (`child_id`,`product_type_id`),
  KEY `FKk7y7veset37n2324sx22q5s` (`product_type_id`),
  CONSTRAINT `FKk7y7veset37n2324sx22q5s` FOREIGN KEY (`product_type_id`) REFERENCES `product_type` (`id`),
  CONSTRAINT `FKoa1lvvoxhh4r4wnqlawuasdsp` FOREIGN KEY (`child_id`) REFERENCES `product_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;