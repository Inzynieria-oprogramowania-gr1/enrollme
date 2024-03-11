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
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8mb3 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Students`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Students` (
  `idStudents` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(60) NULL DEFAULT NULL,
  PRIMARY KEY (`idStudents`))
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`timetable`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`timetable` (
  `idtimetable` INT NOT NULL AUTO_INCREMENT,
  `start_time` TIME NULL DEFAULT NULL,
  `end_time` TIME NULL DEFAULT NULL,
  `isSelectd` BIT(1) NULL DEFAULT b'0',
  `weekDay` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idtimetable`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`Preferences`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Preferences` (
  `Students_idStudents` INT NOT NULL,
  `timetable_idtimetable` INT NOT NULL,
  INDEX `fk_Preferences_Students1_idx` (`Students_idStudents` ASC) VISIBLE,
  INDEX `fk_Preferences_timetable1_idx` (`timetable_idtimetable` ASC) VISIBLE,
  CONSTRAINT `fk_Preferences_Students1`
    FOREIGN KEY (`Students_idStudents`)
    REFERENCES `mydb`.`Students` (`idStudents`),
  CONSTRAINT `fk_Preferences_timetable1`
    FOREIGN KEY (`timetable_idtimetable`)
    REFERENCES `mydb`.`timetable` (`idtimetable`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`Results`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Results` (
  `Students_idStudents` INT NOT NULL,
  `timetable_idtimetable` INT NOT NULL,
  INDEX `fk_Results_Students_idx` (`Students_idStudents` ASC) VISIBLE,
  INDEX `fk_Results_timetable1_idx` (`timetable_idtimetable` ASC) VISIBLE,
  CONSTRAINT `fk_Results_Students`
    FOREIGN KEY (`Students_idStudents`)
    REFERENCES `mydb`.`Students` (`idStudents`),
  CONSTRAINT `fk_Results_timetable1`
    FOREIGN KEY (`timetable_idtimetable`)
    REFERENCES `mydb`.`timetable` (`idtimetable`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;


INSERT INTO Students(email)
VALUES ('zarzitski@student.agh.edu.pl'),
('zolszowka@student.agh.edu.pl'), 
('pkuchta@student.agh.edu.pl'), 
('Asobiesiak@student.agh.edu.pl'),
('amezydlo@student.agh.edu.pl');
Select * from Students;


INSERT INTO timetable(timetable.start_time, end_time, timetable.weekDay)
VALUES ('8:00:00', '9:30:00', 'pn'),
('8:00:00', '9:30:00', 'wt'), 
('8:00:00', '9:30:00', 'sr'), 
('8:00:00', '9:30:00', 'czw'),
('8:00:00', '9:30:00', 'pt');
Select * from timetable;

