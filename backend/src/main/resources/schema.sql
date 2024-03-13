-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`hibernate_sequence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`hibernate_sequence` (
  `next_val` BIGINT NULL DEFAULT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `mydb`.`timetable`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`timetable` (
  `id_timetable` INT NOT NULL,
  `end_time` TIME NULL DEFAULT NULL,
  `is_selected` BIT(1) NOT NULL,
  `start_time` TIME NULL DEFAULT NULL,
  `week_day` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id_timetable`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `mydb`.`students`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`students` (
  `students_id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `slot_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`students_id`),
  INDEX `FKhygspl8a40xs1u5lawbs1o5kk` (`slot_id` ASC) VISIBLE,
  CONSTRAINT `FKhygspl8a40xs1u5lawbs1o5kk`
    FOREIGN KEY (`slot_id`)
    REFERENCES `mydb`.`timetable` (`id_timetable`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `mydb`.`slot_preference`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`slot_preference` (
  `students_id` INT NOT NULL,
  `timetable_id` BIGINT NOT NULL,
  PRIMARY KEY (`students_id`, `timetable_id`),
  INDEX `FKp2tr9h7hfc7g51xxu5riw4cxt` (`timetable_id` ASC) VISIBLE,
  CONSTRAINT `FKg0v2mvc8metvpvvfr2m2sium5`
    FOREIGN KEY (`students_id`)
    REFERENCES `mydb`.`timetable` (`id_timetable`),
  CONSTRAINT `FKp2tr9h7hfc7g51xxu5riw4cxt`
    FOREIGN KEY (`timetable_id`)
    REFERENCES `mydb`.`students` (`students_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
