CREATE SCHEMA IF NOT EXISTS `mybuddy2` ;
USE `mybuddy2` ;

-- -----------------------------------------------------
-- Table `mybuddy2`.`app_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mybuddy2`.`app_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `role_name` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `mybuddy2`.`buddy_account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mybuddy2`.`buddy_account` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `amount_to_charge` DOUBLE NOT NULL,
  `balance` DOUBLE NOT NULL,
  `bank_name` VARCHAR(40) NULL DEFAULT NULL,
  `iban` VARCHAR(27) NULL DEFAULT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `mybuddy2`.`buddy_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mybuddy2`.`buddy_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `date_denaissance` DATE NULL DEFAULT NULL,
  `first_name` VARCHAR(255) NULL DEFAULT NULL,
  `last_name` VARCHAR(255) NULL DEFAULT NULL,
  `pseudo` VARCHAR(255) NULL DEFAULT NULL,
  `buddy_account_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UK_p7oyij8oi2rsa1nga291r72vd` (`pseudo` ASC) VISIBLE,
  INDEX `FK2nvykmdbrvc3uxkvbs1tkeii0` (`buddy_account_id` ASC) VISIBLE,
  CONSTRAINT `FK2nvykmdbrvc3uxkvbs1tkeii0`
    FOREIGN KEY (`buddy_account_id`)
    REFERENCES `mybuddy2`.`buddy_account` (`id`));


-- -----------------------------------------------------
-- Table `mybuddy2`.`app_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mybuddy2`.`app_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `active` BIT(1) NOT NULL,
  `password` VARCHAR(255) NULL DEFAULT NULL,
  `username` VARCHAR(255) NOT NULL,
  `buddy_user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKb2oer6ny15ecfrw997d84t49g` (`buddy_user_id` ASC) VISIBLE,
  CONSTRAINT `FKb2oer6ny15ecfrw997d84t49g`
    FOREIGN KEY (`buddy_user_id`)
    REFERENCES `mybuddy2`.`buddy_user` (`id`));


-- -----------------------------------------------------
-- Table `mybuddy2`.`app_user_app_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mybuddy2`.`app_user_app_roles` (
  `app_user_id` BIGINT NOT NULL,
  `app_roles_id` BIGINT NOT NULL,
  INDEX `FK8caosscox5onsgcvll6tqmk21` (`app_roles_id` ASC) VISIBLE,
  INDEX `FKsno3iwx8ppdc085g7ovuc8h7w` (`app_user_id` ASC) VISIBLE,
  CONSTRAINT `FK8caosscox5onsgcvll6tqmk21`
    FOREIGN KEY (`app_roles_id`)
    REFERENCES `mybuddy2`.`app_role` (`id`),
  CONSTRAINT `FKsno3iwx8ppdc085g7ovuc8h7w`
    FOREIGN KEY (`app_user_id`)
    REFERENCES `mybuddy2`.`app_user` (`id`));


-- -----------------------------------------------------
-- Table `mybuddy2`.`contact`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mybuddy2`.`contact` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `active_status_contact` BIT(1) NOT NULL,
  `id_contact` BIGINT NULL DEFAULT NULL,
  `buddy_user_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FK4k3pat1un67y5dtsockdcca3h` (`buddy_user_id` ASC) VISIBLE,
  CONSTRAINT `FK4k3pat1un67y5dtsockdcca3h`
    FOREIGN KEY (`buddy_user_id`)
    REFERENCES `mybuddy2`.`buddy_user` (`id`));


-- -----------------------------------------------------
-- Table `mybuddy2`.`buddy_user_contacts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mybuddy2`.`buddy_user_contacts` (
  `buddy_user_id` BIGINT NOT NULL,
  `contacts_id` BIGINT NOT NULL,
  UNIQUE INDEX `UK_m8j4su6d4y0qlxstcbc0b8css` (`contacts_id` ASC) VISIBLE,
  INDEX `FKslfqradibaqde32lyydvlnu7d` (`buddy_user_id` ASC) VISIBLE,
  CONSTRAINT `FKmussqk65f1363aphd29bwucew`
    FOREIGN KEY (`contacts_id`)
    REFERENCES `mybuddy2`.`contact` (`id`),
  CONSTRAINT `FKslfqradibaqde32lyydvlnu7d`
    FOREIGN KEY (`buddy_user_id`)
    REFERENCES `mybuddy2`.`buddy_user` (`id`));


-- -----------------------------------------------------
-- Table `mybuddy2`.`transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mybuddy2`.`transaction` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `amount` DOUBLE NOT NULL,
  `date` DATETIME(6) NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `fees` DOUBLE NOT NULL,
  `type` VARCHAR(255) NULL DEFAULT NULL,
  `beneficiary_id` BIGINT NULL DEFAULT NULL,
  `transmitter_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `FKru9g6hjggwovbojisvn90r4qq` (`beneficiary_id` ASC) VISIBLE,
  INDEX `FK6xmveo2tyrf19tyaq1yss06ac` (`transmitter_id` ASC) VISIBLE,
  CONSTRAINT `FK6xmveo2tyrf19tyaq1yss06ac`
    FOREIGN KEY (`transmitter_id`)
    REFERENCES `mybuddy2`.`buddy_user` (`id`),
  CONSTRAINT `FKru9g6hjggwovbojisvn90r4qq`
    FOREIGN KEY (`beneficiary_id`)
    REFERENCES `mybuddy2`.`buddy_user` (`id`));


-- -----------------------------------------------------
-- Table `mybuddy2`.`buddy_user_transactions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mybuddy2`.`buddy_user_transactions` (
  `buddy_user_id` BIGINT NOT NULL,
  `transactions_id` BIGINT NOT NULL,
  INDEX `FKow7r4u453yg1mfjw84hidlmym` (`transactions_id` ASC) VISIBLE,
  INDEX `FK73qaus1r2gr4b6gaj0000ndob` (`buddy_user_id` ASC) VISIBLE,
  CONSTRAINT `FK73qaus1r2gr4b6gaj0000ndob`
    FOREIGN KEY (`buddy_user_id`)
    REFERENCES `mybuddy2`.`buddy_user` (`id`),
  CONSTRAINT `FKow7r4u453yg1mfjw84hidlmym`
    FOREIGN KEY (`transactions_id`)
    REFERENCES `mybuddy2`.`transaction` (`id`));

