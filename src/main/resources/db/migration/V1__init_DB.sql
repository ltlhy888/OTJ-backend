CREATE TABLE `content_key` (
  `id` varchar(255) NOT NULL,
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `content_type` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `file_info` (
  `id` varchar(255) NOT NULL,
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `content_key_id` varchar(255) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `file_type` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `provider` varchar(50) DEFAULT NULL,
  `size` bigint(20) DEFAULT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `access` varchar(50) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_8sewwnpamngi6b1dwaa88askk` (`name`)
);

CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `created` datetime NOT NULL,
  `updated` datetime NOT NULL,
  `activate_token` varchar(255) DEFAULT NULL,
  `activate_token_expired_at` datetime DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `registration_ip` varchar(255) DEFAULT NULL,
  `locked` bit(1) NOT NULL,
  `password` varchar(255) NOT NULL,
  `provider` varchar(255) DEFAULT NULL,
  `provider_id` varchar(255) DEFAULT NULL,
  `password_reset_token` varchar(255) DEFAULT NULL,
  `password_reset_token_expired_at` datetime DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `avatar_id` varchar(255) DEFAULT NULL,
  `last_signed_in` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ob8kqyqqgmefl0aco34akdtpe` (`email`),
  KEY `FKafmqem28ewqb754mfvn6o4bbn` (`avatar_id`),
  CONSTRAINT `FKafmqem28ewqb754mfvn6o4bbn` FOREIGN KEY (`avatar_id`) REFERENCES `file_info` (`id`)
) ;


CREATE TABLE `user_roles` (
  `user_id` varchar(255) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKrhfovtciq1l558cw6udg0h0d3` (`role_id`),
  CONSTRAINT `FK55itppkw3i07do3h7qoclqd4k` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKrhfovtciq1l558cw6udg0h0d3` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ;


