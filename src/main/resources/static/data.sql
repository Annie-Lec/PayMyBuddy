
CREATE DATABASE IF NOT EXISTS `mybuddy2`;

use `mybuddy2`;

CREATE TABLE `app_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
  ) ;
  
  CREATE TABLE `buddy_account` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount_to_charge` double NOT NULL,
  `balance` double NOT NULL,
  `bank_name` varchar(40) DEFAULT NULL,
  `iban` varchar(27) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
  CREATE TABLE `buddy_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date_denaissance` date DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `pseudo` varchar(255) DEFAULT NULL,
  `buddy_account_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_p7oyij8oi2rsa1nga291r72vd` (`pseudo`),
  KEY `FK2nvykmdbrvc3uxkvbs1tkeii0` (`buddy_account_id`),
  CONSTRAINT `FK2nvykmdbrvc3uxkvbs1tkeii0` FOREIGN KEY (`buddy_account_id`) 
  REFERENCES `buddy_account` (`id`)
) ;
  CREATE TABLE `app_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `buddy_user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKb2oer6ny15ecfrw997d84t49g` (`buddy_user_id`),
  CONSTRAINT `FKb2oer6ny15ecfrw997d84t49g` FOREIGN KEY (`buddy_user_id`) 
  REFERENCES `buddy_user` (`id`)
) ;
CREATE TABLE `app_user_app_roles` (
  `app_user_id` bigint NOT NULL,
  `app_roles_id` bigint NOT NULL,
  KEY `FK8caosscox5onsgcvll6tqmk21` (`app_roles_id`),
  KEY `FKsno3iwx8ppdc085g7ovuc8h7w` (`app_user_id`),
  CONSTRAINT `FK8caosscox5onsgcvll6tqmk21` FOREIGN KEY (`app_roles_id`) REFERENCES `app_role` (`id`),
  CONSTRAINT `FKsno3iwx8ppdc085g7ovuc8h7w` FOREIGN KEY (`app_user_id`) REFERENCES `app_user` (`id`)
) ;

CREATE TABLE `contact` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active_status_contact` bit(1) NOT NULL,
  `id_contact` bigint DEFAULT NULL,
  `buddy_user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4k3pat1un67y5dtsockdcca3h` (`buddy_user_id`),
  CONSTRAINT `FK4k3pat1un67y5dtsockdcca3h` FOREIGN KEY (`buddy_user_id`) REFERENCES `buddy_user` (`id`)
) ;
CREATE TABLE `buddy_user_contacts` (
  `buddy_user_id` bigint NOT NULL,
  `contacts_id` bigint NOT NULL,
  UNIQUE KEY `UK_m8j4su6d4y0qlxstcbc0b8css` (`contacts_id`),
  KEY `FKslfqradibaqde32lyydvlnu7d` (`buddy_user_id`),
  CONSTRAINT `FKmussqk65f1363aphd29bwucew` FOREIGN KEY (`contacts_id`) REFERENCES `contact` (`id`),
  CONSTRAINT `FKslfqradibaqde32lyydvlnu7d` FOREIGN KEY (`buddy_user_id`) REFERENCES `buddy_user` (`id`)
);
CREATE TABLE `transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `fees` double NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `beneficiary_id` bigint DEFAULT NULL,
  `transmitter_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKru9g6hjggwovbojisvn90r4qq` (`beneficiary_id`),
  KEY `FK6xmveo2tyrf19tyaq1yss06ac` (`transmitter_id`),
  CONSTRAINT `FK6xmveo2tyrf19tyaq1yss06ac` FOREIGN KEY (`transmitter_id`) REFERENCES `buddy_user` (`id`),
  CONSTRAINT `FKru9g6hjggwovbojisvn90r4qq` FOREIGN KEY (`beneficiary_id`) REFERENCES `buddy_user` (`id`)
);
CREATE TABLE `buddy_user_transactions` (
  `buddy_user_id` bigint NOT NULL,
  `transactions_id` bigint NOT NULL,
  KEY `FKow7r4u453yg1mfjw84hidlmym` (`transactions_id`),
  KEY `FK73qaus1r2gr4b6gaj0000ndob` (`buddy_user_id`),
  CONSTRAINT `FK73qaus1r2gr4b6gaj0000ndob` FOREIGN KEY (`buddy_user_id`) REFERENCES `buddy_user` (`id`),
  CONSTRAINT `FKow7r4u453yg1mfjw84hidlmym` FOREIGN KEY (`transactions_id`) REFERENCES `transaction` (`id`)
);


INSERT INTO `mybuddy2`.`app_role`
(`id`,
`description`,
`role_name`)
VALUES
(1, "profil a renseigner", "1PROFIL_TO_DEF"),
(2, "contact a renseigner", "2CONTACT_TO_DEF"),
(3, "utilisateur ayant acces à toutes les fonctionnalites", "3USER")
;

INSERT INTO `mybuddy2`.`buddy_account` 
(`amount_to_charge`, `balance`, `bank_name`, `iban`) 
VALUES 
(0,0,'',''),
( 0, 10, 'BNPP', 'FR4945457545454545454545492'),
( 0, 0, '', ''),
( 0, 0, '', ''),
( 0, 0, 'BANQUE POPULAIRE', 'FR4945454545754545454545495'),
( 0, 0, 'HSBC', 'FR4945454545474545454545496'),
( 0, 107, 'BNPP', 'FR4945454545454745454545497')
;

INSERT INTO `mybuddy2`.`buddy_user` 
(`date_denaissance`, `first_name`, `last_name`, `pseudo`, `buddy_account_id`) 
VALUES 
(null, '', '', 'bank', '1'),
('1982-02-12', 'Roger', 'RABBIT', 'roger@gmail.com', 2),
('1983-03-13', 'Bugs', 'Bunny', 'buddy2@email.fr',3),
('1984-04-14', 'Mike', 'Mouse', 'mickey@yahoo.fr', 4),
('1985-05-15', 'Donald', 'DUCK', 'dodo@gmail.com', 5),
('1986-06-16', 'Dingo', 'DOG', 'dingue@email.fr', 6),
('1987-07-17', 'Minie', 'MOUSE', 'minie@yahoo.fr', 7)
;

INSERT INTO `mybuddy2`.`app_user` 
(`active`, `password`, `username`, `buddy_user_id`) 
VALUES 
(1, '$2a$10$SJbJXCn6JsmudoqtZr6xSuPRQTqSZ0NYxUgchLUK6AqOURbfElz0W', 'bank@bank.com', 1),
(1, '$2a$10$Ue1hjTBZOGpAp8RFwjW9r.MonyAIbDBDdD.8Ovpc2EFbMGmIT1QM6', 'roger@gmail.com', 2),
(1,  '$2a$10$TBQB.eqftzLWSjOm74U9Ceh3IffPKKS22Kstpfhz7smHSfNrQFtPO', 'buddy2@email.fr', 3),
(1, '$2a$10$IltNj4E4CnHHddDNMQoG3uZJu5/s4VbS87pi1Sr5Xg7w00a8Pys5m', 'mickey@yahoo.fr', 4),
(1, '$2a$10$T7UR95IlFlbEqghQGEvCP.yzKKZzbvzldCYINpxT8MIaciFiTFJeC', 'dodo@gmail.com', 5),
(1, '$2a$10$HWJqD6.3DxEeydIu1t1laeqwgowMIOw9zMsqcZEirT7q1kfiP2puy', 'dingue@email.fr', 6),
(1, '$2a$10$3OGnQUqCtWP68oWd2XV5Bu2xE0wTuNgXjKXhkEg.yyPIRXGFVlbFO', 'minie@yahoo.fr', 7)
;


INSERT INTO `mybuddy2`.`app_user_app_roles`
(`app_user_id`,
`app_roles_id`)
VALUES
(2, 3),
(3, 1),
(4, 1),
(5, 1),
(6, 1),
(7, 2),
(7, 3)
;

INSERT INTO `mybuddy2`.`contact` 
(`active_status_contact`, `id_contact`, `buddy_user_id`) 
VALUES 
(1, 7, 2),
(1, 5, 7),
(1, 6, 7)
;


INSERT INTO `mybuddy2`.`buddy_user_contacts` 
(`buddy_user_id`, `contacts_id`) 
VALUES 
(2, 1),
(7, 2),
(7, 3)
;

INSERT INTO `mybuddy2`.`transaction` 
(`amount`, `date`, `description`, `fees`, `beneficiary_id`, `transmitter_id`) 
VALUES 
(10, '2023-01-11 16:58:16.037000', '10 pour Roger', 0.5, 2, 7)
;


INSERT INTO `mybuddy2`.`buddy_user_transactions` 
(`buddy_user_id`, `transactions_id`) 
VALUES 
(2, 1),
(7, 1)
;








