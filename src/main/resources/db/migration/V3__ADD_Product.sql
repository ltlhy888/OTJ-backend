CREATE TABLE `product_type` (
  `id` bigint(20) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_bnu2aqss00w6he2vs4bmmy609` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `contact_method_set` (
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `contact_method` (
  `id` bigint(20) NOT NULL,
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `contact` varchar(255) NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  `type` int(11) NOT NULL,
  `contact_method_set_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKruse0mknbrsu30gqilkr75j31` (`contact_method_set_id`),
  CONSTRAINT `FKruse0mknbrsu30gqilkr75j31` FOREIGN KEY (`contact_method_set_id`) REFERENCES `contact_method_set` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `follow` (
  `id` bigint(20) NOT NULL,
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `content_key_id` varchar(255) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `which_id` varchar(255) DEFAULT NULL,
  `which_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `comment_set` (
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL,
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `enabled` tinyint(1) NOT NULL DEFAULT 1,
  `msg` varchar(300) NOT NULL,
  `rate` int(11) NOT NULL,
  `comment_set_id` bigint(20) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkuyhf62guuoa13dpsjgo3no11` (`comment_set_id`),
  KEY `FK8kcum44fvpupyw6f5baccx25c` (`user_id`),
  CONSTRAINT `FK8kcum44fvpupyw6f5baccx25c` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKkuyhf62guuoa13dpsjgo3no11` FOREIGN KEY (`comment_set_id`) REFERENCES `comment_set` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `product` (
  `id` varchar(255) NOT NULL,
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `description` varchar(2000) DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `comment_set_id` bigint(20) DEFAULT NULL,
  `contact_method_set_id` bigint(20) DEFAULT NULL,
  `current_price_id` bigint(20) DEFAULT NULL,
  `image_content_id` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) NOT NULL,
  `additional_data_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5ajq1dsp09nmbaqqmi77io9nj` (`comment_set_id`),
  KEY `FKf99ah59gkyrgemerie83u7jb8` (`contact_method_set_id`),
  KEY `FKo5uji84jprglqyhbv4ds2eehh` (`image_content_id`),
  KEY `FK979liw4xk18ncpl87u4tygx2u` (`user_id`),
  KEY `FKglyy07hl3gf9vhv31dcurf08o` (`additional_data_id`),
  CONSTRAINT `FK5ajq1dsp09nmbaqqmi77io9nj` FOREIGN KEY (`comment_set_id`) REFERENCES `comment_set` (`id`),
  CONSTRAINT `FK979liw4xk18ncpl87u4tygx2u` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKf99ah59gkyrgemerie83u7jb8` FOREIGN KEY (`contact_method_set_id`) REFERENCES `contact_method_set` (`id`),
  CONSTRAINT `FKglyy07hl3gf9vhv31dcurf08o` FOREIGN KEY (`additional_data_id`) REFERENCES `content_key` (`id`),
  CONSTRAINT `FKo5uji84jprglqyhbv4ds2eehh` FOREIGN KEY (`image_content_id`) REFERENCES `content_key` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `product_and_types` (
  `product_id` varchar(255) NOT NULL,
  `product_type_id` bigint(20) NOT NULL,
  PRIMARY KEY (`product_id`,`product_type_id`),
  KEY `FKk7y7veset37ns9yp05do219q5` (`product_type_id`),
  CONSTRAINT `FKk7y7veset37ns9yp05do219q5` FOREIGN KEY (`product_type_id`) REFERENCES `product_type` (`id`),
  CONSTRAINT `FKoa1lvvoxhh4r4wnql0d56rusp` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `price` (
  `id` bigint(20) NOT NULL,
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `currency` varchar(50) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `price` bigint(20) DEFAULT NULL,
  `price_type` varchar(255) DEFAULT NULL,
  `priority` int(11) NOT NULL,
  `status` varchar(50) NOT NULL,
  `product_id` varchar(255) NOT NULL,
  `user_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKk4mbgqf5yru5ib5b6w5l6ukkj` (`product_id`),
  KEY `FKs13dswkc5n8832crl1kohoggq` (`user_id`),
  CONSTRAINT `FKk4mbgqf5yru5ib5b6w5l6ukkj` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKs13dswkc5n8832crl1kohoggq` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE `transaction` (
  `id` varchar(255) NOT NULL,
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `currency` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `third_party_id` varchar(255) DEFAULT NULL,
  `third_party_type` varchar(50) DEFAULT NULL,
  `user_key` varchar(255) DEFAULT NULL,
  `price_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_third_party` (`third_party_id`,`third_party_type`),
  KEY `FK863hx45x9bh79kl9xoxw0lkhs` (`price_id`),
  CONSTRAINT `FK863hx45x9bh79kl9xoxw0lkhs` FOREIGN KEY (`price_id`) REFERENCES `price` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

ALTER TABLE product
ADD KEY `FK3c7ybtagdy39ablg61qr0q6md` (`current_price_id`),
ADD CONSTRAINT `FK3c7ybtagdy39ablg61qr0q6md` FOREIGN KEY (`current_price_id`) REFERENCES `price` (`id`);

