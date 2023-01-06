CREATE TABLE IF NOT EXISTS `gifts`.`certificates` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(45) NULL DEFAULT NULL,
  `price` DECIMAL(10,2) UNSIGNED NULL DEFAULT NULL,
  `duration` INT NULL DEFAULT NULL,
  `create_date` TIMESTAMP NULL DEFAULT NULL,
  `last_update_date` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)

CREATE TABLE IF NOT EXISTS `gifts`.`tags` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)

CREATE TABLE IF NOT EXISTS `gifts`.`certificates_has_tags` (
  `certificates_id` INT NOT NULL,
  `tags_id` INT NOT NULL,
  PRIMARY KEY (`certificates_id`, `tags_id`),
  INDEX `fk_certificates_has_tags_tags1_idx` (`tags_id` ASC) VISIBLE,
  INDEX `fk_certificates_has_tags_certificates_idx` (`certificates_id` ASC) VISIBLE,
  CONSTRAINT `fk_certificates_has_tags_certificates`
    FOREIGN KEY (`certificates_id`)
    REFERENCES `gifts`.`certificates` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_certificates_has_tags_tags1`
    FOREIGN KEY (`tags_id`)
    REFERENCES `gifts`.`tags` (`id`)
    ON DELETE CASCADE)
