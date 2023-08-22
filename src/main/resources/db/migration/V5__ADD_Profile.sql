CREATE TABLE `profile` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `preferred_contact_method` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `contact_method_set_id` bigint(20) DEFAULT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6kaxc2imcn5fkvl7n4sy7a74y` (`contact_method_set_id`),
  KEY `FKawh070wpue34wqvytjqr4hj5e` (`user_id`),
  CONSTRAINT `FK6kaxc2imcn5fkvl7n4sy7a74y` FOREIGN KEY (`contact_method_set_id`) REFERENCES `contact_method_set` (`id`),
  CONSTRAINT `FKawh070wpue34wqvytjqr4hj5e` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;